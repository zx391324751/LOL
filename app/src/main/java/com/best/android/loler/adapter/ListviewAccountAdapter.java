package com.best.android.loler.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.best.android.loler.R;
import com.best.android.loler.config.NetConfig;
import com.best.android.loler.dao.AccountDao;
import com.best.android.loler.manager.PhotoManager;
import com.best.android.loler.model.Account;
import com.best.android.loler.view.CircleImageView;
import com.best.android.loler.view.ScrollerLinearLayout;

import java.util.List;

/**
 * Created by BL06249 on 2015/12/16.
 */
public class ListviewAccountAdapter extends BaseAdapter {

    private Context context;
    private List<Account>listAcount;
    private RequestQueue mQueue;

    public ListviewAccountAdapter(Context context, List<Account>listAccount) {
        this.context = context;
        this.listAcount = listAccount;
        this.mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return listAcount.size();
    }

    @Override
    public Object getItem(int position) {
        return listAcount.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountViewHolder accountViewHolder = null;

        if(convertView == null){
            View view01 = LayoutInflater.from(context).inflate(R.layout.adapter_item_account, parent, false);
            View view02 = LayoutInflater.from(context).inflate(R.layout.listview_scroller_right_item, parent, false);

            view02.findViewById(R.id.scroller_right_ll_delete).setOnClickListener(
                    new OnAccountItemClickListener(OnAccountItemClickListener.CLICK_DELETE, position));
            view02.findViewById(R.id.scroller_right_ll_check).setOnClickListener(
                    new OnAccountItemClickListener(OnAccountItemClickListener.CLICK_CHECK, position));

            accountViewHolder = new AccountViewHolder();
            accountViewHolder.ivPhoto = (CircleImageView) view01.findViewById(R.id.adapter_item_account_iv_photo);
            accountViewHolder.tvName = (TextView) view01.findViewById(R.id.adapter_item_account_tv_name);
            accountViewHolder.tvServer = (TextView) view01.findViewById(R.id.adapter_item_account_tv_server);
            accountViewHolder.ivCheck = (ImageView)view01.findViewById(R.id.adapter_item_account_iv_check);

            ScrollerLinearLayout scrollerLinearLayout = new ScrollerLinearLayout(context);
            scrollerLinearLayout.setContentView(view01, view02);
            convertView = scrollerLinearLayout;
            convertView.setTag(accountViewHolder);
        } else {
            accountViewHolder = (AccountViewHolder)convertView.getTag();
        }

        bindData(accountViewHolder, position);
        return convertView;
    }

    private void loadImage(final AccountViewHolder holder, final String url) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.ivPhoto.setSrcBitmap(response);
                        PhotoManager.getInstance().addBitmapToMemCache(url, response, true);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        mQueue.add(imageRequest);

    }

    private void bindData(AccountViewHolder holder, int position) {
        holder.tvServer.setText(listAcount.get(position).accountServerName
                + " / " + listAcount.get(position).tierDesc
                + " / " + listAcount.get(position).accountLevel + "级");
        holder.tvName.setText(listAcount.get(position).accountName + "   战斗力:" + listAcount.get(position).fightLevel);

        if(listAcount.get(position).isSelect){
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }

        String url = NetConfig.getLolAccountPhotoUrl(listAcount.get(position).photoId);
        Bitmap bitmap = PhotoManager.getInstance().getBitmapFromMemCache(url);
        if(bitmap == null)
            loadImage(holder, url);
        else{
            holder.ivPhoto.setSrcBitmap(bitmap);
        }
    }

    private class AccountViewHolder{
        public CircleImageView ivPhoto;
        public TextView tvName;
        public TextView tvServer;
        public ImageView ivCheck;
    }

    class OnAccountItemClickListener implements View.OnClickListener{

        public static final int CLICK_DELETE = -1;
        public static final int CLICK_CHECK = 1;

        int clickType;
        int position;
        public OnAccountItemClickListener(int clickType, int position){
            this.clickType = clickType;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (clickType){
                case CLICK_DELETE:
                    showDeleteDialog(position);
                    return;
                case CLICK_CHECK:
                    check(position);
                    return;
            }

        }
    }

    private void showDeleteDialog(final int deletePosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除提示!");
        builder.setMessage("是否解除绑定该账号?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(deletePosition);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    //更新选择的数据
    private void check(int position) {
        if(listAcount.get(position).isSelect)
            return;
        AccountDao accountDao = new AccountDao();
        for(int i=0; i<listAcount.size(); i++){
            if(listAcount.get(i).isSelect){
                listAcount.get(i).isSelect = false;
                accountDao.updateAccount(listAcount.get(i));
            }
        }
        listAcount.get(position).isSelect = true;
        accountDao.updateAccount(listAcount.get(position));
        this.notifyDataSetChanged();
    }

    //删除数据
    public void delete(int position){
        AccountDao accountDao = new AccountDao();
        accountDao.deleteAccount(listAcount.get(position));
        listAcount.remove(position);
        this.notifyDataSetChanged();
    }

}
