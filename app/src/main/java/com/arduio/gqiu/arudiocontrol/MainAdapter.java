package com.arduio.gqiu.arudiocontrol;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
 class MainAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mCxt;
    private List<BluetoothDevice> mList;
    private BluetoothConnectionListener mListener;


     MainAdapter(Context context, BluetoothConnectionListener listener) {
        mCxt = context;
        mListener = listener;
    }


    public void setData(Set<BluetoothDevice> list) {
        Iterator<BluetoothDevice> iter = list.iterator();
        mList = new ArrayList<>();
        while (iter.hasNext()) {
            BluetoothDevice item = iter.next();
            mList.add(item);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCxt).inflate(R.layout.item_bluetooth, parent, false);
            holder = new ViewHolder();
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.btnConnect = (Button) convertView.findViewById(R.id.btn_connect);
            holder.btnConnect.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice item = mList.get(position);
        holder.btnConnect.setTag(item);

        holder.tvAddress.setText(item.getAddress());
        holder.tvName.setText(item.getName());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        BluetoothDevice tag = (BluetoothDevice) v.getTag();
        mListener.connection(tag);
    }


    private class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        Button btnConnect;

    }


    interface BluetoothConnectionListener {
        void connection(BluetoothDevice device);
    }
}
