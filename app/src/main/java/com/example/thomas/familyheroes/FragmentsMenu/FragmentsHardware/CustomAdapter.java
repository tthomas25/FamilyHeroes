package com.example.thomas.familyheroes.FragmentsMenu.FragmentsHardware;

/**
 * Created by Thomas on 08/11/2014.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.familyheroes.Classes.Hardware;
import com.example.thomas.familyheroes.R;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter{

    /*********** Declare Used Variables *********/
    ArrayList data;
    Context context;


    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Context ctx, ArrayList d) {

       this.context = ctx;
       this.data = d;
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView nom;
        public TextView lieu;
        public ImageView image;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView==null){

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.liste_hardware, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.nom = (TextView) convertView.findViewById(R.id.nom);
            holder.lieu=(TextView)convertView.findViewById(R.id.lieu);
            holder.image=(ImageView)convertView.findViewById(R.id.icon);

            /************  Set holder with LayoutInflater ************/
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(data.size()<=0)
        {
            holder.nom.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/

            Hardware hardware_values = ( Hardware ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.nom.setText( hardware_values.getNom() );
            holder.lieu.setText( hardware_values.getLieu() );


            if(hardware_values.getNom()=="bracelet" && hardware_values.isEtat())
            {
                holder.image.setImageResource(R.drawable.bracelet_ok);
            }
            else if(hardware_values.getNom()=="bracelet" && !hardware_values.isEtat())
            {
                holder.image.setImageResource(R.drawable.bracelet_no);
            }
            else if(hardware_values.getNom()=="kinect" && hardware_values.isEtat())
            {
                holder.image.setImageResource(R.drawable.bracelet_ok);
            }
            else if(hardware_values.getNom()=="kinect" && !hardware_values.isEtat())
            {
                holder.image.setImageResource(R.drawable.bracelet_no);
            }



        }
        return convertView;
    }

}