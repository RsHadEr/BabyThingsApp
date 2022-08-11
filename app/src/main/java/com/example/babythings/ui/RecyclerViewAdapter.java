package com.example.babythings.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babythings.R;
import com.example.babythings.data.DatabaseHandler;
import com.example.babythings.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.awt.font.TextAttribute;
import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context=context;
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row,parent,false);
        return new ViewHolder(view,context);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position);
        holder.itemName.setText(MessageFormat.format("Item:{0}", item.getItemName()));
        holder.itemColor.setText(MessageFormat.format("Color:{0}", item.getItemColor()));
        holder.quantity.setText(MessageFormat.format("Quantity:{0}", Integer.toString(item.getItemQuantity())));
        holder.size.setText(MessageFormat.format("Size:{0}", Integer.toString(item.getItemSize())));
        holder.dateAdded.setText(MessageFormat.format("Date_Added:{0}", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemColor;
        public TextView quantity;
        public TextView size;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;



        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            itemName=itemView.findViewById(R.id.item_name);
            itemColor=itemView.findViewById(R.id.item_color);
            quantity=itemView.findViewById(R.id.item_quantity2);
            size=itemView.findViewById(R.id.item_size);
            dateAdded=itemView.findViewById(R.id.item_date);
            editButton=itemView.findViewById(R.id.edit_button);
            deleteButton=itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           int position = getAdapterPosition();
           Item item =itemList.get(position);
            Log.d("position", "onClick: "+item.getId());
            switch (view.getId()){
                case R.id.edit_button:
                    editItem(item);

                    break;

                case R.id.delete_button:
                    deleteItem(item.getId());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }

        }

        private void deleteItem(int id) {




            DatabaseHandler databaseHandler =new DatabaseHandler(context);
            itemList.remove(id);
            databaseHandler.deleteItem(id);

            notifyItemRemoved(id);
        }
        private void editItem(Item newItem) {
            Item item =itemList.get(getAdapterPosition());
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText babyItem;
            final EditText itemQuantity;
            final EditText itemColor;
            final EditText itemSize;
            TextView title;

            babyItem = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemColor = view.findViewById(R.id.itemColor);
            itemSize = view.findViewById(R.id.itemSize);
            saveButton = view.findViewById(R.id.save_button);
            saveButton.setText(R.string.update_text);
            title = view.findViewById(R.id.tile);


            title.setText(R.string.edit_time);
            babyItem.setText(newItem.getItemName());
            itemQuantity.setText(String.valueOf(newItem.getItemQuantity()));
            itemColor.setText(newItem.getItemColor());
            itemSize.setText(String.valueOf(newItem.getItemSize()));

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    newItem.setItemName(babyItem.getText().toString());
                    newItem.setItemColor(itemColor.getText().toString());
                    newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    newItem.setItemSize(Integer.parseInt(itemSize.getText().toString()));


                    if(!babyItem.getText().toString().isEmpty()
                            &&!itemColor.getText().toString().isEmpty()
                    &&!itemQuantity.getText().toString().isEmpty()
                            &&!itemSize.getText().toString().isEmpty())
                    {
                        db.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(),newItem);
                    }
                    else{
                        Snackbar.make(view,"empty",Snackbar.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();


                }
            });

        }
    }


}
