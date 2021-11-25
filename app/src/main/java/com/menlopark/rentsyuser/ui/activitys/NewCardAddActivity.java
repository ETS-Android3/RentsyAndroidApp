package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.Utils;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

public class NewCardAddActivity extends AppCompatActivity {
    Activity act;

    private CardInputWidget cardHolderNumber;
    private TextView newCard;
    Dialog loader;
    ApiService apiService;

    private void assignViews() {

        newCard = (TextView) findViewById(R.id.btn_new_card);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_add);
        act = NewCardAddActivity.this;
        getSupportActionBar().setTitle("ADD CARD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardHolderNumber = (CardInputWidget) findViewById(R.id.cardHolderNumber);
        loader = new Dialog(act);
        apiService = ApiClient.getClient(PreferenceUtil.getPref(this).getString(Constants.pref_App_Token, "")).create(ApiService.class);
        assignViews();
        newCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardHolderNumber != null) {
                    if (cardHolderNumber.getCard() != null)
                        getCardData(cardHolderNumber.getCard());
                    else
                        setAlertMessage("The card number that you entered is invalid.");
                }
            }
        });
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void getCardData(Card card) {
        if (loader != null && !loader.isShowing() && !act.isFinishing()) {
            loader.show();
        }
        if (validateCard(card)) {
            createStripeToken(card);
        } else {
            if (loader != null && loader.isShowing() && !act.isFinishing()) {
                loader.dismiss();
            }
        }
    }

    public boolean validateCard(Card card) {
        boolean validation = card.validateCard();
        if (validation) {
            return true;
        } else if (!card.validateNumber()) {

            setAlertMessage("The card number that you entered is invalid.");
            return false;
        } else if (!card.validateExpiryDate()) {

            setAlertMessage("The expiration date that you entered is invalid.");
            return false;
        } else if (!card.validateCVC()) {
            setAlertMessage("The CVV code that you entered is invalid.");
            return false;
        } else {
            setAlertMessage("The card details that you entered are invalid.");
            return false;

        }

    }


    public void createStripeToken(Card card) {
        Stripe stripe = new Stripe(act, Utils.publishable_Key);
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        Log.e("strip token", token.getId());
                       // Toast.makeText(act, token.getId() + "", Toast.LENGTH_SHORT).show();
                        /*if (loader != null && loader.isShowing() && !act.isFinishing()) {
                            loader.dismiss();
                        }*/
                        //sendCardToInfoToStripe(token.getId());
                       call_getListOfCards(token.getId());
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        if (loader != null && loader.isShowing() && !act.isFinishing()) {
                            loader.dismiss();
                        }
                        Toast.makeText(act,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    public void setAlertMessage(String msg) {
        Toast.makeText(act, msg + "", Toast.LENGTH_SHORT).show();
    }

    public void call_getListOfCards(String token) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("customer_id", Integer.parseInt(Config.getPreference(Constants.pref_CustomerId)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Data :", jsonObject.toString());
        ServerCall.getResponse(act, "addPayment.json", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                if (loader != null && loader.isShowing() && !act.isFinishing()) {
                    loader.dismiss();
                }
              //  Toast.makeText(act, result, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(result);

                    if (obj.getInt("status") == 200) {
                        showAlert();
                    } else {
                     //   Toast.makeText(act, "Error : " + obj.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                }


            }

            @Override
            public void onError(String error) {
                if (loader != null && loader.isShowing() && !act.isFinishing()) {
                    loader.dismiss();
                }
                Toast.makeText(act, "Error : " + error + "", Toast.LENGTH_SHORT).show();
            }
        });

/* NewCard newCard = new NewCard();
        newCard.setCustomerId(Integer.parseInt(Config.getPreference(Constants.pref_CustomerId)));
        newCard.setToken(token);

        Log.e("Data :", newCard.toString());
        Call<NewCardResponse> call = apiService.addPayment(newCard);

        call.enqueue(new Callback<NewCardResponse>() {
            @Override
            public void onResponse(Call<NewCardResponse> call, Response<NewCardResponse> response) {
                if (loader != null && loader.isShowing() && !act.isFinishing()) {
                    loader.dismiss();
                }

                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                        showAlert();
                    } else {
                        Toast.makeText(act, "Error : " + response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        NewCardResponse newCardResponse = new Gson().fromJson(response.errorBody().string(), NewCardResponse.class);
                        Log.e("Error: =======>   ", newCardResponse.getMessage() + "");
                        Toast.makeText(act, "Error :" + newCardResponse.getMessage() + "", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("IOException : =======>", e.getMessage() + "");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewCardResponse> call, Throwable t) {
                if (loader != null && loader.isShowing() && !act.isFinishing()) {
                    loader.dismiss();
                }
                Log.e("Error:     =======>   ", t.getMessage() + "");

            }
        });
* */
    }

    public void showAlert() {
        new AlertDialog.Builder(act)
                .setTitle("Success")
                .setMessage("Your payment details have been added successfully")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}