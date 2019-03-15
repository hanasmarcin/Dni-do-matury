package hanas.dnidomatury.examActivity.sheetList;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import hanas.dnidomatury.R;
import hanas.dnidomatury.examActivity.DataViewModel;
import hanas.dnidomatury.examActivity.CustomLayoutManager;
import hanas.dnidomatury.model.examSpecific.ExamItemsList;
import hanas.dnidomatury.model.examSpecific.sheet.Sheet;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheetListFragment extends Fragment {

    private ExamItemsList<Sheet> sheetsList;
    private RecyclerView recyclerView;
    private SheetAdapter adapter;

    public SheetListFragment() {
        // Required empty public constructor
    }

    public static SheetListFragment newInstance() {
        // Create new fragment with variables passed via bundle
        System.out.println(111);
        Bundle args = new Bundle();
        SheetListFragment fragment = new SheetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view from xml
        return inflater.inflate(R.layout.fragment_sheet_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the data from ViewModel, which is available for parent fragment and its' fragments
        DataViewModel data = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        sheetsList = data.getSheets();
        recyclerView = view.findViewById(R.id.sheets_recycler_view);
        CoordinatorLayout coordinator = view.findViewById(R.id.sheet_list_coordinator);

        // Set up the recyclerView
        adapter = new SheetAdapter(this, sheetsList, coordinator);
        CustomLayoutManager customLayoutManager = new CustomLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(customLayoutManager);
        // After the recyclerView is set up, update the observable loading state in data,
        // that fragment is loaded and ready to be swiped to
        data.loadingState.setSheetListFragmentLoaded(true);

    }

    private void addSheet(Bundle bundle) {
        // Get sheet/s properties from bundle
        String sheetName = bundle.getString("sheetName");
        String sheetDateText = bundle.getString("sheetDateText");
        double points = bundle.getDouble("points");
        double maxPoints = bundle.getDouble("maxPoints");

        // Create new sheet and add it to the list
        final Sheet newSheet = new Sheet(sheetName, sheetDateText, points, maxPoints);
        sheetsList.add(0, newSheet);

        // Move sheet to the correct position on the list and get this position
        int newPosition = 0; //sheetsList.moveAndSort(1, true);
        recyclerView.scrollToPosition(newPosition);
        if (adapter != null) adapter.notifyItemInserted(newPosition);
    }

    private void editSheet(Bundle bundle) {
        // Get sheet/s properties from bundle
        String sheetName = bundle.getString("sheetName");
        String sheetDateText = bundle.getString("sheetDateText");
        double points = bundle.getDouble("points");
        double maxPoints = bundle.getDouble("maxPoints");
        int sheetID = bundle.getInt("sheetID");
        System.out.println(sheetName+sheetDateText+points+" "+maxPoints+sheetID);
        // Find the sheet, that needs to be edited
        Sheet editedSheet = sheetsList.get(sheetID);
        // Check, if it's date was edited
//        boolean dateChanged = false;
//        if (!editedSheet.getSheetDateText().equals(sheetDateText)) dateChanged = true;
        // Update sheet's properties
        editedSheet.update(sheetName, sheetDateText, points, maxPoints);
        if (adapter != null) adapter.notifyItemChanged(sheetID);

        // If sheet's date was edited, move sheet to the correct position
//        if (dateChanged) {
//            int newSheetPos = sheetsList.moveAndSort(sheetID, false);
//            if (newSheetPos == sheetID)
//                newSheetPos = sheetsList.moveAndSort(sheetID, true);
//            if (adapter != null) adapter.notifyItemMoved(sheetID, newSheetPos);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // Get data provided by finished dialogFragment
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            // Depending on requested action, add or edit sheet
            if (requestCode == ADD_SHEET_REQUEST_CODE && resultCode == RESULT_OK)
                addSheet(bundle);
            else if (requestCode == EDIT_SHEET_REQUEST_CODE && resultCode == RESULT_OK)
                editSheet(bundle);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_exam, menu);
    }

    private static final int ADD_SHEET_REQUEST_CODE = 9831;
    final static int EDIT_SHEET_REQUEST_CODE = 1190;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_task) {
            // If ADD button on the toolbar was clicked, start new dialogFragment
            AddSheetFragment addSheetDialog = AddSheetFragment.forAdd();
            addSheetDialog.setTargetFragment(this, ADD_SHEET_REQUEST_CODE);
            addSheetDialog.show(getFragmentManager(), "WTF4");
        }
        return super.onOptionsItemSelected(item);
    }
}
