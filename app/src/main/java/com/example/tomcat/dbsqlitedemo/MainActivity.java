package com.example.tomcat.dbsqlitedemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static android.support.v7.appcompat.R.styleable.AlertDialog;

public class MainActivity extends AppCompatActivity
{
    private ListView item_list;
    private TextView show_app_name;

    private ItemAdapter itemAdapter;
    private List<Item>  items;
    private MenuItem add_item, search_item, revert_item, delete_item;
    private int selectedCount = 0;
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about);
        setContentView(R.layout.activity_main);

        processViews();
        processControllers();

        itemDAO = new ItemDAO(getApplicationContext());
        if (itemDAO.getCount() == 0)
        {
            itemDAO.sample();
        }

        items = itemDAO.getAll();

        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            Item item = (Item) data.getExtras().getSerializable("com.example.tomcat.dbsqlitedemo.Item");

            if (requestCode == 0)
            {
                item = itemDAO.insert(item);
                items.add(item);
                itemAdapter.notifyDataSetChanged();
            }
            else if (requestCode == 1)
            {
                int position = data.getIntExtra("position", -1);
                if (position != -1)
                {
                    itemDAO.update(item);

                    items.set(position, item);
                    itemAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void processViews()
    {
        item_list = (ListView)findViewById(R.id.item_list);
        show_app_name = (TextView)findViewById(R.id.show_app_name);
    }

    private void processControllers()
    {
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Item item = itemAdapter.getItem(position);
                if (selectedCount > 0)
                {
                    processMenu(item);
                    itemAdapter.set(position, item);
                }
                else
                {
                    Intent intent = new Intent("com.example.tomcat.dbsqlitedemo.EDIT_ITEM");
                    intent.putExtra("position", position);
                    intent.putExtra("com.example.tomcat.dbsqlitedemo.Item", item);

                    startActivityForResult(intent, 1);
                }
            }
        };

        item_list.setOnItemClickListener(itemListener);

        AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Item item = itemAdapter.getItem(position);
                processMenu(item);
                itemAdapter.set(position, item);
                return true;
            }
        };
        item_list.setOnItemLongClickListener(itemLongListener);

        View.OnLongClickListener listener = new View.OnLongClickListener()
        {
            public boolean onLongClick(View view)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("SQLite Demo").setMessage("DataBase SQLite demo app.").show();
                return false;
            }
        };
        show_app_name.setOnLongClickListener(listener);
    }

    private void processMenu(Item item)
    {
        if (item != null)
        {
            item.setSelected(!item.isSelected());
            if (item.isSelected())
            {
                selectedCount++;
            }
            else
            {
                selectedCount--;
            }
        }

        add_item.setVisible(selectedCount == 0);
        search_item.setVisible(selectedCount == 0);
        revert_item.setVisible(selectedCount > 0);
        delete_item.setVisible(selectedCount > 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        delete_item = menu.findItem(R.id.delete_item);

        processMenu(null);
        return true;
    }

    public void clickMenuItem(MenuItem item)
    {
        int itemId = item.getItemId();

        switch (itemId)
        {
            case R.id.search_item:
                break;

            case R.id.add_item:
                Intent intent = new Intent("com.example.tomcat.dbsqlitedemo.ADD_ITEM");
                startActivityForResult(intent, 0);
                break;

            case R.id.revert_item:
                for (int i=0; i<itemAdapter.getCount(); i++)
                {
                    Item ri = itemAdapter.getItem(i);
                    if (ri.isSelected())
                    {
                        ri.setSelected(false);
                        itemAdapter.set(i, ri);
                    }
                }
                selectedCount = 0;
                processMenu(null);
                break;

            case R.id.delete_item:
                if (selectedCount == 0)
                {
                    break;
                }

                AlertDialog.Builder d = new AlertDialog.Builder(this);
                String message = "Are you sure to delete ?";
                d.setTitle("Delete").setMessage(String.format(message, selectedCount));
                d.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        int index = itemAdapter.getCount() - 1;
                        while (index > -1)
                        {
                            Item item = itemAdapter.get(index);
                            if (item.isSelected())
                            {
                                itemAdapter.remove(item);
                                itemDAO.delete(item.getId());
                            }
                            index--;
                        }
                        itemAdapter.notifyDataSetChanged();
                    }
                });
                d.setNegativeButton(android.R.string.no, null);
                d.show();
                break;
        }
    }

    public void aboutApp(View view)
    {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void clickPreferences(MenuItem item)
    {
        startActivity(new Intent(this, PrefActivity.class));
    }

}
