package hanas.dnidomatury.examActivity.SheetList;


import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.model.ExamSpecificList;
import hanas.dnidomatury.model.ExamsFileList;
import hanas.dnidomatury.model.matura.Exam;
import hanas.dnidomatury.model.matura.ExamsList;
import hanas.dnidomatury.model.sheet.Sheet;
import hanas.dnidomatury.model.sheet.SheetsList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheetListFragment extends Fragment {

    ExamSpecificList<Sheet> sheetList;
    Exam mSelectedExam;
    int selectedExamPOS;
    RecyclerView recyclerView;
    SheetAdapter adapter;
    NestedScrollView nested;
    ExamsFileList mListOfExam;
    int primaryColorID;
    CoordinatorLayout examCoordinator;

    public SheetListFragment() {
        // Required empty public constructor

    }

    public static SheetListFragment newInstance(int selectedExamPOS) {

        Bundle args = new Bundle();
        args.putInt("selectedExamPOS", selectedExamPOS);
        SheetListFragment fragment = new SheetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedExamPOS = bundle.getInt("selectedExamPOS");
            mSelectedExam = ExamsList.fromFile(true, getActivity()).get(selectedExamPOS);
            //sheetList = SheetsList.fromFile(getActivity(), ExamsList.fromFile(true, getActivity()).get(selectedExamPOS));
        }
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        sheetList = data.getSheets();

//            mListOfExam = SelectedExamsList.getInstance(getActivity());
//            mSelectedExam = mListOfExam.get(selectedExamPOS);
//            primaryColorID = mSelectedExam.getPrimaryColorID(getActivity());
//            sheetList = SheetsList.fromFile(getActivity(), mSelectedExam);
        //sheetList = FileSupport.fromFile(getActivity(), "");

        //sheetList = new SheetsList(mSelectedExam.getName(), mSelectedExam.getType(), mSelectedExam.getLevel(), getActivity());
        //sheetList.readFromFile(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_sheet_list, container, false);

        nested = rootView.findViewById(R.id.nested_sheet);
        recyclerView = rootView.findViewById(R.id.sheets_recycler_view);
        examCoordinator = rootView.findViewById(R.id.sheet_list_coordinator);
        recyclerView.setHasFixedSize(true);
        CustomLayoutManager layoutManager = new CustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SheetAdapter(this, sheetList, examCoordinator);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        return rootView;
    }


    public double getSheetsAverage() {
        if (sheetList.size() == 0) return -1;
        else {
            double sheetsAverage = 0;
            for (int i = 0; i < sheetList.size(); i++) {
                Sheet sheet = sheetList.get(i);
                sheetsAverage += sheet.getPoints() / sheet.getMaxPoints() * 100;
            }
            sheetsAverage /= sheetList.size();
            return sheetsAverage;
        }
    }

    @Override
    public void onPause() {
        //System.out.println("ROZMIAR SZIETLISTY "+sheetList.size());
        super.onPause();
        //sheetList.toFile(getActivity(), mSelectedExam);
        //mListOfExam = ListOfExam.readFromFile(getActivity(),true);
        //saveData();
    }

    public class CustomLayoutManager extends LinearLayoutManager {

        public CustomLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return true;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 55) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String sheetName = bundle.getString("sheetName");
                    String sheetDateText = bundle.getString("sheetDateText");
                    double points = bundle.getDouble("points");
                    double maxPoints = bundle.getDouble("maxPoints");
                    //int sheetID = bundle.getInt("sheetID");

                    final Sheet newSheet = new Sheet(sheetName, sheetDateText, points, maxPoints);
                    sheetList.add(newSheet);

                    adapter.notifyDataSetChanged();
                    sheetList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        } else if (requestCode == 5439) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String sheetName = bundle.getString("sheetName");
                    String sheetDateText = bundle.getString("sheetDateText");
                    double points = bundle.getDouble("points");
                    double maxPoints = bundle.getDouble("maxPoints");
                    int sheetID = bundle.getInt("sheetID");

                    final Sheet newSheet = new Sheet(sheetName, sheetDateText, points, maxPoints);
                    sheetList.remove(sheetID);
                    sheetList.add(newSheet);
                    adapter.notifyItemInserted(sheetID);
                    adapter.notifyDataSetChanged();
                    sheetList.sort();
                    nested.scrollTo(0, 0);

                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            Intent intent = new Intent(getActivity(), AddSheetActivity.class);
            startActivityForResult(intent, 55);
        }
        return super.onOptionsItemSelected(item);
    }
}
