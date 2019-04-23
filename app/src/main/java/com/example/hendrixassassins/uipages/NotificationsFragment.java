package com.example.hendrixassassins.uipages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.hendrixassassins.AgentProfileActivity;
import com.example.hendrixassassins.R;
import com.example.hendrixassassins.agent.Agent;
import com.example.hendrixassassins.email.Email;
import com.example.hendrixassassins.email.MessageReader;
import com.example.hendrixassassins.email.Notification;
import com.example.hendrixassassins.email.NotificationList;

import java.io.EOFException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View fragView;
    private Button showListView;

//    private ListView notificationListView;
    private NotificationList notificationList;
    private ArrayList<Notification> allNotifications = new ArrayList<>();

    private ArrayList<Email> inboxEmails = new ArrayList<>();
    private MessageReader messageReader;


    public NotificationsFragment() {
        // Required empty public constructor


        notificationList = new NotificationList();
        notificationList.addNotification(new Notification(new Agent("aperson@hendrix.edu", "Patrick"), "Iwant to dodd"));
        notificationList.addNotification(new Notification(new Agent("aperson@hendrix.edu", "kakanana"), "Iwant to dodd"));
        notificationList.addNotification(new Notification(new Agent("aperson@hendrix.edu", "kamnana"), "Iwant to dodd"));
        allNotifications = notificationList.getAllNotifications();
        Log.e("Im in here", "I am called11");
        updateMessages();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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


//        mySwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
//
//                        // This method performs the actual data-refresh operation.
//                        // The method calls setRefreshing(false) when it's finished.
//                        myUpdateOperation();
//                    }
//                }
//        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_notifications, container, false);
        createListViewAdapter();
//        showListView = fragView.findViewById(R.id.showListView_Button);
        return fragView;
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


//    public void createViewAdapter(){
//        notificationListView = getView().findViewById(R.id.notifications_ListView);
//    }

    private void createListViewAdapter(){
        ListView notificationListView = fragView.findViewById(R.id.notifications_ListView);
        NotificationListViewAdapter adapter = new NotificationListViewAdapter<>(this.getContext(),
                R.layout.test_list_view, allNotifications);
        notificationListView.setAdapter(adapter);

        setUpItemClickListener(notificationListView);

        Log.e("size of inbox message", Integer.toString(inboxEmails.size()));
        Log.e("createdlistViewAdapter", "Created list view adapter");
    }

    private void setUpItemClickListener(ListView listView){
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = allNotifications.get(position).getNotifier().getEmail();
                toNotificationContentActivity(email);
            }

        });
    }

    private void toNotificationContentActivity(String email) {
        Intent forwardIntent = new Intent(getActivity(), NotificationTemplateViewActivity.class);
        forwardIntent.putExtra("clickedUserEmail", email);
        startActivity(forwardIntent);
    }

    public void updateMessages(){
        Log.e("Im in here", "I am called11");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    messageReader = new MessageReader("HendrixAssassinsApp@gmail.com", "AssassinsTest1");
                    Log.e("connedted", "I connected");
                    inboxEmails = messageReader.getInboxMessages();
                    Log.e("sise of messagelist", String.valueOf(inboxEmails.size()));
                }catch (Exception e){
                    Log.e("messea fil", "message Reder failed");
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
////        notificationListView = (ListView) view.findViewById(R.id.notifications_ListView);
//    }

}