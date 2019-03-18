package hanas.dnidomatury.examListActivity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hanas.dnidomatury.R;
import hanas.dnidomatury.model.exam.ExamsList;
import hanas.dnidomatury.model.exam.SelectedExamsList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExamListFragment extends Fragment {


    private ExamAdapter adapter;
    private ExamsList selectedExams;

    public ExamListFragment() {
        // Required empty public constructor
    }

    static ExamListFragment newInstance() {
        // Create new Fragment, needed properties may be added via intent
        return new ExamListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exam_list, container, false);

        // Get the selected exams list
        selectedExams = SelectedExamsList.getInstance(getActivity());
        // Set the recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.selected_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new ExamAdapter(getActivity(), selectedExams);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // When the activity is resumed, exams properties may be changed by other activities
        // So they are updated
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        selectedExams.toFile(getActivity(), true);
        super.onPause();
    }
}
