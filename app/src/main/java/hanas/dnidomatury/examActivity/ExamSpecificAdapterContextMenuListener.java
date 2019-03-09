package hanas.dnidomatury.examActivity;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public interface ExamSpecificAdapterContextMenuListener extends View.OnCreateContextMenuListener {

    boolean onMenuDeleteClick(int adapterPosition);
    boolean onMenuEditClick(int adapterPosition);
    int getAdapterPosition();

    @Override
    default void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem deleteMenuItem = menu.add(Menu.NONE, 1, 1, "Usuń");
        MenuItem editMenuItem = menu.add(Menu.NONE, 2, 2, "Edytuj");

        deleteMenuItem.setOnMenuItemClickListener(menuItem -> onMenuDeleteClick(getAdapterPosition()));
        editMenuItem.setOnMenuItemClickListener(menuItem -> onMenuEditClick(getAdapterPosition()));
    }
}
