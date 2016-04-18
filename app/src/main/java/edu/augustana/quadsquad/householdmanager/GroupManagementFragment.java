package edu.augustana.quadsquad.householdmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
//import com.google.android.gms.appinvite.AppInviteInvitation;
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.services.people.v1.People;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupManagementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupManagementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final int REQUEST_INVITE = 13;
    final int PICK_CONTACT = 14;
    final int GET_EMAIL = 15;
    private final String TAG = "GroupManagement";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnInvites;
    private OnFragmentInteractionListener mListener;

    private ListViewCompat membersList;
    private ListViewCompat invitesList;

    private Firebase ref;
    private FirebaseListAdapter<Member> memberAdapter;

    public GroupManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupManagementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupManagementFragment newInstance(String param1, String param2) {
        GroupManagementFragment fragment = new GroupManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Firebase.setAndroidContext(getContext());
        ref = new Firebase("https://household-manager-136.firebaseio.com");


    }

    private void onInviteClicked() {
        new MaterialDialog.Builder(getContext())
                .title("New Invite")
                .content("Please enter the email address of the housemate you wish to invite.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Email Address", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        postInvite(input.toString());
                    }
                }).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_management, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnInvites = (Button) getView().findViewById(R.id.invite_button);
        btnInvites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInviteClicked();
            }
        });

        invitesList = (ListViewCompat) getView().findViewById(R.id.invites_list_view);

        membersList = (ListViewCompat) getView().findViewById(R.id.members_list_view);

        bindListViews();

    }

    private void bindListViews() {
        Firebase memberRef = ref.child("users");
        String groupID = SaveSharedPreference.getPrefGroupId(getContext());
        Query memberQuery = memberRef.orderByChild("groupReferal").startAt(groupID).endAt(groupID);

        memberAdapter = new FirebaseListAdapter<Member>(getActivity(), Member.class, R.layout.avatar_list_item, memberQuery) {
            @Override
            protected void populateView(View view, Member member, int position) {

                ((TextView) view.findViewById(R.id.name_view)).setText(member.getDisplayName());


                String photoURL = member.getContactPicURI();
                CircleImageView avatar = (CircleImageView) view.findViewById(R.id.avatar);

                if (!photoURL.equals("")) {
                    Picasso.with(getContext()).load(photoURL).fit().into(avatar);
                }


            }
        };
        membersList.setAdapter(memberAdapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void postInvite(String email) {
        Context ctx = getContext();
        Firebase mFirebaseInvites = new Firebase("https://household-manager-136.firebaseio.com/invites");
        Invite newInvite = new Invite(SaveSharedPreference.getHouseName(ctx), SaveSharedPreference.getGoogleEmail(ctx),
                email, SaveSharedPreference.getGooglePictureUrl(ctx), SaveSharedPreference.getPrefGroupId(ctx));
        mFirebaseInvites.push().setValue(newInvite);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
