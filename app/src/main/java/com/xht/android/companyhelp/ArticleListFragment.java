package com.xht.android.companyhelp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xht.android.companyhelp.model.Article;
import com.xht.android.companyhelp.net.APIListener;
import com.xht.android.companyhelp.net.VolleyHelpApi;
import com.xht.android.companyhelp.util.LogHelper;
import com.xht.android.companyhelp.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";

    private ListView mListView;
    private ArrayList<Article> mArticles = new ArrayList<>();
    private int witchF;
    private Bitmap mPlaceHolderBitmap;

    private SimpleDateFormat mSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Drawable mBlueDrawable, mGreenDrawable, mRedDrawable, mGrayDrawable;

    public ArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        witchF = bundle.getInt("witchF", 0);
        LogHelper.i(TAG, "witchF=" + witchF);
        mPlaceHolderBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.p_head);
        mBlueDrawable = getResources().getDrawable(R.drawable.blue_stroke);
        mGreenDrawable = getResources().getDrawable(R.drawable.yellow_stroke);
        mRedDrawable = getResources().getDrawable(R.drawable.red_stroke);
        mGrayDrawable = getResources().getDrawable(R.drawable.gray_stroke);
        fetchItemTask(witchF);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articlelist, container, false);
        mListView = (ListView) view.findViewById(R.id.articleListV);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempS = "http://www.xiaohoutai.com.cn:8888/XHT/ArticleinfoGetController/findArticleNews?id=" +
                        mArticles.get(position).getId();
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("article_url", tempS);
                getActivity().startActivity(intent);
            }
        });
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        if (getActivity() == null || mListView == null) {
            return;
        }
        if (mArticles != null) {
            mListView.setAdapter(new ArticleAdapter(mArticles));
        } else {
            mListView.setAdapter(null);
        }
    }

    /**
     * 向服务器获取这类文章
     *
     * @param witch 类别
     */
    private void fetchItemTask(int witch) {
        VolleyHelpApi.getInstance().getArticleItems(witch, new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, result.toString());
                parseArticles((JSONArray) result);
                setupAdapter();
            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private void parseArticles(JSONArray jA) {
        int artId;
        Article article;
        String artT;
        String imgUrl;
        String sLB;
        JSONObject jO;
        int length = jA.length();
        for (int i = 0; i < length; i++) {
            try {
                jO = jA.getJSONObject(i);
                artId = jO.getInt("artid");
                artT = jO.getString("title");
                sLB = jO.getString("subArtTypeName");
                LogHelper.i(TAG, "小类别=" + sLB);
                imgUrl = "http://www.xiaohoutai.com.cn:8888/XHT/" + jO.getString("imageurl").replaceAll("\\\\", "/");
                LogHelper.i(TAG, "imgUrl=" + imgUrl);
                long pbTime = Long.parseLong(jO.getString("pbtime"));
                article = new Article(artId, artT, pbTime, null, imgUrl, sLB);
                mArticles.add(article);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ArticleAdapter extends ArrayAdapter<Article> {

        public ArticleAdapter(ArrayList<Article> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.article_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.artTitle_item);
                holder.pbT = (TextView) convertView.findViewById(R.id.timeTV);
                holder.smallLeiBie = (TextView) convertView.findViewById(R.id.smallLeiBieTV);
                holder.imgV = (ImageView) convertView.findViewById(R.id.artPic);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Article item = getItem(position);
            holder.title.setText(item.getTitle());
            switch (item.getmSmallLeiBie()) {
                case "办证":
                    holder.smallLeiBie.setBackgroundDrawable(mBlueDrawable);
                    holder.smallLeiBie.setTextColor(Color.BLUE);
                    break;
                case "发票":
                    holder.smallLeiBie.setBackgroundDrawable(mGreenDrawable);
                    holder.smallLeiBie.setTextColor(Color.GREEN);
                    break;
                case "注册":
                    holder.smallLeiBie.setBackgroundDrawable(mRedDrawable);
                    holder.smallLeiBie.setTextColor(Color.RED);
                    break;
                default:
                    holder.smallLeiBie.setBackgroundDrawable(mGrayDrawable);
                    holder.smallLeiBie.setTextColor(Color.GRAY);
                    break;
            }
            holder.smallLeiBie.setText(item.getmSmallLeiBie());

            holder.pbT.setText(mSDF.format(new Date(item.getmShijian())));
//            holder.imgV.setImageResource(R.mipmap.phone_sym_img);
            //异步加载图片
            //String urlRegex = "http://i.imgur.com/7spzG.png".replaceAll("[^a-z0-9]", "");
            String urlRegex = item.getArtPicUrl().replaceAll("[^a-z0-9]", "");
            LogHelper.i(TAG, "urlRegex=" + urlRegex);
            Bitmap bm = App.getInstance().getLruCacheManager().getLruImageCache()
                    .getBitmap(urlRegex);
            if (bm != null)
                holder.imgV.setImageBitmap(bm);
            else
                loadBitmap(item.getArtPicUrl(), holder.imgV);
            return convertView;
        }

    }

    public void loadBitmap(String resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private String data;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return getPicFromNet(data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private Bitmap getPicFromNet(String data) {
        InputStream datas = Utils.httpGetArtSmallPic(data);
        Bitmap bitmap = BitmapFactory.decodeStream(datas);
        if (bitmap != null)
            App.getInstance().getLruCacheManager().getLruImageCache()
                    .putBitmap(data.replaceAll("[^a-z0-9]", ""), bitmap);
        return bitmap;
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    class ViewHolder {
        public TextView title;
        public TextView pbT;
        public TextView smallLeiBie;
        public ImageView imgV;
    }

}
