package com.menlopark.rentsyuser.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.server_access.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by DELL on 2/1/2018.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.SettingItemsViewHolder> {

    Boolean flag = false;
    private List<CardInfo> cards;
    private int rowLayout;
    private Context context;
    private OnCardClickListener listener;


    public CardListAdapter(List<CardInfo> cards, int rowLayout, Context context, OnCardClickListener clickListener) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = clickListener;


    }

    @Override
    public void onBindViewHolder(final SettingItemsViewHolder holder, final int position) {
        holder.card_num.setText("XXXX XXXX XXXX " + cards.get(position).getLast4());
        holder.card_expiry.setText(cards.get(position).getExpMonth() + "/" + cards.get(position).getExpYear());
        holder.country.setText(cards.get(position).getCountry());

        if (cards.get(position).getDefaultCard() == true) {
            holder.default_card.setImageResource(R.drawable.tick_green);
            Config.setPreference(Constants.pref_Default_Card_Last4, cards.get(position).getLast4());
        } else {
            holder.default_card.setImageResource(R.drawable.ic_radiobtn_off);
        }


        holder.default_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cards.get(position).getDefaultCard() != true) {
                    call_setAsDefaultCard(cards.get(position).getId(), holder, position);
                } else {
                    Toast.makeText(context, "Card is already selected as default.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ic_delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_DeleteCard(cards.get(position).getId(), holder, position);
            }
        });
    }

    @Override
    public SettingItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SettingItemsViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void call_setAsDefaultCard(String card_id, SettingItemsViewHolder holder, int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", Config.getPreference(Constants.pref_CustomerId));
            jsonObject.put("card_id", card_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall.getResponse(context, "make-as-default-card", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject obj = new JSONObject(result);

                    if (obj.getInt("status") == 200) {
                        for (int i = 0; i < cards.size(); i++) {
                            if (i == position) {
                                cards.get(i).setDefaultCard(true);

                            } else {
                                cards.get(i).setDefaultCard(false);
                            }
                        }
                        notifyDataSetChanged();
                        Toast.makeText(context, obj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, obj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void call_DeleteCard(String card_id, SettingItemsViewHolder holder, int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", Config.getPreference(Constants.pref_CustomerId));
            jsonObject.put("card_id", card_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall.getResponse(context, "remove-card", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject obj = new JSONObject(result);

                    if (obj.getInt("status") == 200) {
                        for (int i = 0; i < cards.size(); i++) {
                            if (i == position) {
                                cards.remove(i);
                            }
                        }
                        notifyDataSetChanged();
                        Toast.makeText(context, obj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, obj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class SettingItemsViewHolder extends RecyclerView.ViewHolder {
        //        LinearLayout cardsLayout;
        TextView card_num, card_expiry, country;
        AppCompatImageView default_card;
        RelativeLayout front_card_container;
        ImageView ic_delete_card;


        @SuppressLint("WrongViewCast")
        public SettingItemsViewHolder(View v) {
            super(v);
            card_num = v.findViewById(R.id.front_card_number);
            card_expiry = v.findViewById(R.id.front_card_expiry);
            country = v.findViewById(R.id.front_card_holder_country);
            default_card = v.findViewById(R.id.default_card);
            front_card_container = v.findViewById(R.id.front_card_container);
            ic_delete_card = v.findViewById(R.id.ic_delete_card);

        }
    }
}
