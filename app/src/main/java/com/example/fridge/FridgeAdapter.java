package com.example.fridge;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FridgeAdapter extends RecyclerView.Adapter<FridgeAdapter.Viewholder> {
    private ArrayList<Food> FoodList;
    Context context;
    final String URL = "https://dataapi.moc.go.th/gis-product-prices?product_id=";
    String updatePrice;

    public FridgeAdapter(ArrayList<Food> foodList, Context context) {
        FoodList = foodList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.foodform, parent,false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView1.setText(FoodList.get(position).getFoodName());
        holder.textView3.setText(FoodList.get(position).getQuantity()+" ชิ้น");
        holder.textView4.setText("หมดอายุ : "+FoodList.get(position).getFoodEXP());

        if (FoodList.get(position).getFoodType().equals("เนื้อ")){
            holder.imageView1.setImageResource(R.drawable.meat_100px);
        }
        else if (FoodList.get(position).getFoodType().equals("ผัก/ผลไม้")){
            holder.imageView1.setImageResource(R.drawable.organic_food_100px);
        }
        else if (FoodList.get(position).getFoodType().equals("อาหารสำเร็จรูป")){
            holder.imageView1.setImageResource(R.drawable.noodles_100px);
        }
        else if (FoodList.get(position).getFoodType().equals("อื่นๆ")){
            holder.imageView1.setImageResource(R.drawable.help_100px);
        }

        if (!FoodList.get(position).getCanCheckPrice().equals("False")){
            Date date = new Date();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String sURL = URL+FoodList.get(position).getCanCheckPrice()+"&from_date="+currentDate+"&to_date="+currentDate;
            new MyAsyncTask(sURL,holder.textView2).execute();
        }
    }

    @Override
    public int getItemCount() {
        return FoodList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textView1,textView2,textView3,textView4;
        Button button1;
        ImageView imageView1;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView5);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView2);
            button1 = itemView.findViewById(R.id.button3);
            imageView1 = itemView.findViewById(R.id.imageView);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBhelper dBhelper = new DBhelper(context);
                    SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
                    sqLiteDatabase.execSQL("DELETE FROM foodlist WHERE id="+FoodList.get(getAdapterPosition()).getId());
                    FoodList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    class MyAsyncTask extends AsyncTask<Integer,Integer,String> {
        String cURL ;
        TextView textView ;

        public MyAsyncTask(String cURL, TextView textView) {
            this.cURL = cURL;
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory,trustManager)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        })
                        .build();

                Request request = new Request.Builder().url(cURL).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String[] parts = s.split(",");
            String maxAvg = parts[8].replace("\"price_max_avg\":","");
            String unit = parts[6].replace("\"unit\":","").replaceAll("\"","");
            updatePrice = maxAvg+" "+unit;
            textView.setText(updatePrice);
        }
    }
}
