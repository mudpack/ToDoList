package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.widget.AdapterView;

import java.util.ArrayList;

public class ToDoListActivity extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        myListView = (ListView)findViewById(R.id.myListView);
        myEditText = (EditText)findViewById(R.id.myEditText);

        todoItems = new ArrayList<String>();
        int resID = R.layout.todolist_item;
        aa = new ArrayAdapter<String>(this, resID, todoItems);
        //final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

        myListView.setAdapter(aa);

        myEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction() == KeyEvent.ACTION_DOWN ) {
                    if ( keyCode == KeyEvent.KEYCODE_ENTER ) {
                        todoItems.add(0, myEditText.getText().toString());
                        aa.notifyDataSetChanged();
                        myEditText.setText("");
                        cancelAdd();

                        return true;
                    }
                }

                return false;
            }

        });

        registerForContextMenu(myListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE, R.string.add_new);
        itemAdd.setIcon(R.drawable.add_new_item);
        itemAdd.setShortcut('0', 'a');

        MenuItem itemRemove = menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
        itemRemove.setIcon(R.drawable.remove_item);
        itemRemove.setShortcut('1', 'r');

        return true;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);    //To change body of overridden methods use File | Settings | File Templates.

        menu.setHeaderTitle("Select to do item");
        menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);    //To change body of overridden methods use File | Settings | File Templates.

        final String removeTitle = getString(addingNew ? R.string.cancel : R.string.remove);

        MenuItem removeItem = menu.findItem(REMOVE_TODO);
        removeItem.setTitle(removeTitle);
        removeItem.setVisible(addingNew || myListView.getCount() > 0 );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.

        int index = myListView.getSelectedItemPosition();

        switch (item.getItemId()) {
            case (REMOVE_TODO): {
                if ( addingNew ) {
                    cancelAdd();
                }
                else {
                    if ( index != ListView.INVALID_POSITION )
                        removeItem(index);
                }
                return true;
            }
            case ( ADD_NEW_TODO ): {
                addNewItem();
                return true;
            }
        }

        return false;
    }

    private void cancelAdd() {
        addingNew = false;
        myEditText.setVisibility(View.GONE);
    }

    private void addNewItem() {
        addingNew = true;
        myEditText.setVisibility(View.VISIBLE);
        myEditText.requestFocus();
    }

    private void removeItem(int _index) {
        assert (_index >= 0 && _index < todoItems.size());

        todoItems.remove(_index);
        aa.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.

        switch ( item.getItemId() ) {
            case (REMOVE_TODO) : {
                AdapterView.AdapterContextMenuInfo menuInfo;
                menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                int index = menuInfo.position;

                removeItem(index);
                return true;
            }
        }

        return false;
    }

    static final private int ADD_NEW_TODO = Menu.FIRST;
    static final private int REMOVE_TODO = Menu.FIRST + 1;

    private boolean addingNew = false;
    private ArrayList<String> todoItems;
    private ListView myListView;
    private EditText myEditText;
    private ArrayAdapter<String> aa;

}
