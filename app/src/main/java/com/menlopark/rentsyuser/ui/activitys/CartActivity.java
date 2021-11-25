package com.menlopark.rentsyuser.ui.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ServerCall;
import com.menlopark.rentsyuser.model.Add_Booking.AddBookingRes;
import com.menlopark.rentsyuser.model.Booking.BookingList;
import com.menlopark.rentsyuser.model.Booking.BookingParams;
import com.menlopark.rentsyuser.model.Cart_Detail.CartDetailRes;
import com.menlopark.rentsyuser.model.Cart_Detail.Cart_Detail_Params;
import com.menlopark.rentsyuser.model.Promocode.PromocodeRes;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartRes;
import com.menlopark.rentsyuser.model.add_to_cart.CartInfo;
import com.menlopark.rentsyuser.model.dummy.CategoryContent;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.Dialog;
import com.menlopark.rentsyuser.util.TwoDigitsCardTextWatcher;
import com.menlopark.rentsyuser.util.Utils;
import com.menlopark.rentsyuser.view.adapters.CartItemListAdapter;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class CartActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "cart_id";
    private static final String ARG_PARAM2 = "param2";
    String cart_id, sDiscount, sBookingFees;
    String mParam2;
    TextView tv_msg;

    CategoryContent.CategoryItem item;
    CartItemListAdapter cartItemListAdapter;

    ApiService apiService;
    Dialog loader;
    String token;
    android.app.Dialog dialog;
    String a;
    int keyDel;

    EditText edtExpiry, edtCard, edtCVV;
    Button btnSubmit;
    int total;
    String sPayment_token;
    RelativeLayout rv_contain;
    int promoApply = 0;

    AddToCartRes addToCartRes;
    List<CartInfo> cartInfos = new ArrayList<>();
    Gson gson;

    int pro_discount, pro_dis_type;
    Activity act;
    View view;
    AlertDialog.Builder deleteDialog;
    private TextView detailTitle;
    private RecyclerView item_recyclerview;
    private AppCompatImageView detailImage;
    private TextView detailDescTitle;
    private TextView detailDesc;
    private View view2;
    private TextView sub_totals;
    private TextView booking_fees;
    private TextView discounts;
    private TextView detailsTotalCostTwo;
    private EditText detailsMadterNumber;
    private EditText edtPromoCode;
    private Button btnApplyPromo;
    private Button btn_make_booking;
public int swipedItemPos;
    private Button btn_add_more_into_cart;
    private void assignViews() {

        detailTitle = findViewById(R.id.detail_title);
        item_recyclerview = findViewById(R.id.item_recyclerview);
        detailImage = findViewById(R.id.detail_image);
        detailDescTitle = findViewById(R.id.detail_desc_title);
        detailDesc = findViewById(R.id.detail_desc);


        view2 = findViewById(R.id.view2);

        sub_totals = findViewById(R.id.sub_totals);
        booking_fees = findViewById(R.id.booking_fees);
        discounts = findViewById(R.id.discounts);

        detailsTotalCostTwo = findViewById(R.id.details_total_cost_two);
        detailsMadterNumber = findViewById(R.id.details_madter_number);
        edtPromoCode = findViewById(R.id.edtPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btn_make_booking = findViewById(R.id.btn_make_booking);
        rv_contain = findViewById(R.id.rv_contain);
        tv_msg = findViewById(R.id.tv_note);
        btn_add_more_into_cart = findViewById(R.id.btn_add_more_into_cart);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);
        act = CartActivity.this;
        assignViews();
        init();
        if (getIntent().hasExtra("cart_id")) {
            cart_id = getIntent().getStringExtra("cart_id");
        }
        getSupportActionBar().setTitle("CART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtPromoCode.getText().toString();
        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate_FieldsProm()) {
                    postToServer();
                }
            }
        });

        btn_add_more_into_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        btn_make_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Call_Booking();
                }
            }
        });

        detailsMadterNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //card_entry_dialog();
                startActivity(new Intent(act, CardListActivity.class));
            }
        });
    }


    private boolean validate_FieldsProm() {
        if (TextUtils.isEmpty(edtPromoCode.getText().toString())) {
            detailsMadterNumber.requestFocus();
            Toast.makeText(act, "Enter PromoCode", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void init() {

        token = PreferenceUtil.getPref(act).getString(Constants.pref_App_Token, "");
        gson = new Gson();
        Constants.APP_TOKEN =token;
        apiService = ApiClient.getClient(token).create(ApiService.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        item_recyclerview.setLayoutManager(layoutManager);
        loader = new Dialog(act);


        Paint paint = new Paint();
        initDialog();

        //Paint paint = null;

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                try {

                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 5;
                        viewHolder.itemView.setTranslationX(dX / 5);

                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX / 5, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        Bitmap icon = BitmapFactory.decodeResource(act.getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((itemView.getRight() + dX / 7), (float) itemView.getTop() + width, (float) itemView.getRight() + dX / 20, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, paint);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            /*@Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }*/

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //onSwipeLogic(viewHolder, true);

                swipedItemPos = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    removeView();
                    deleteDialog.setTitle("Delete Item");
                    deleteDialog.show();

                } else {

                }
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(item_recyclerview);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initDialog() {
        deleteDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText("Do you really want to delete this item?");
        deleteDialog.setView(view);
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                call_DeleteCartItem(addToCartRes.getData().getCartInfo().get(swipedItemPos).getCartId());
                cartItemListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cartItemListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        deleteDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cartItemListAdapter.notifyDataSetChanged();
            }
        });
        deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cartItemListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    public void call_DeleteCartItem(Integer cart_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cart_id", cart_id);
            jsonObject.put("customer_id", Config.getPreference(Constants.pref_CustomerId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall.getResponse(act, "removeItemInCart.json", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try
                {
                    dialog.hide();
                }catch(Exception e)
                {
                }

                //PreferenceUtil.getPref(act).edit().putString(Constants.pref_Cart_Response, new Gson().toJson(result)).commit();

                for(int i=0;i<addToCartRes.getData().getCartInfo().size();i++){
                    if(addToCartRes.getData().getCartInfo().get(i).getCartId()==cart_id){
                        addToCartRes.getData().getCartInfo().remove(i);
                    }
                }
                PreferenceUtil.getPref(act).edit().putString(Constants.pref_Cart_Response, new Gson().toJson(addToCartRes)).commit();
                setCartDetails();
            }

            @Override
            public void onError(String error) {
                try
                {
                    dialog.hide();
                }catch(Exception e)
                {
                }

                Toast.makeText(act, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartDetails();


    }


    private void setCartDetails() {
        addToCartRes = new Gson().fromJson(PreferenceUtil.getPref(act).getString(Constants.pref_Cart_Response, ""), AddToCartRes.class);
        if (addToCartRes != null) {

            cartInfos = addToCartRes.getData().getCartInfo();
            cartItemListAdapter = new CartItemListAdapter(act, cartInfos);
            item_recyclerview.setAdapter(cartItemListAdapter);
            Integer sub_total = 0;
            for (int i = 0; i < cartInfos.size(); i++) {
                sub_total += cartInfos.get(i).getAmount();
            }
            sub_totals.setText("$" + sub_total + "");
        } else {
            rv_contain.setVisibility(View.GONE);
            tv_msg.setVisibility(View.VISIBLE);
            Toast.makeText(act, "No Data found..!!", Toast.LENGTH_SHORT).show();
        }
        detailsMadterNumber.setText("XXXX-XXXX-XXXX-" + Config.getPreference(Constants.pref_Default_Card_Last4));
    }


    private void call_Get_Cart_Detail(final int promo_Apply) {
        Cart_Detail_Params cart_detail_params = new Cart_Detail_Params();
        cart_detail_params.setCart_id(cart_id);


        if (loader != null && !loader.isShowing() && !isFinishing()) {
            loader.show();
        }
        Call<CartDetailRes> call = apiService.call_Get_Cart_Detail(cart_detail_params);
        call.enqueue(new retrofit2.Callback<CartDetailRes>() {
            @Override
            public void onResponse(Call<CartDetailRes> call, retrofit2.Response<CartDetailRes> response) {
                if (response.body().getStatus() == 200) {
                    if (loader != null && loader.isShowing() && !isFinishing()) {
                        loader.dismiss();
                    }
                    Picasso.with(act).load(response.body().getData().getDetails().getImages()).placeholder(R.drawable.white_bg).into(detailImage);
                    detailTitle.setText(response.body().getData().getDetails().getBusinessName());
                    detailDescTitle.setText(response.body().getData().getDetails().getLocationName());
                    detailDesc.setText(response.body().getData().getDetails().getBusinessAddress());


                    sDiscount = response.body().getData().getDetails().getDiscount().toString();

                    sBookingFees = response.body().getData().getDetails().getBookingFee().toString();
                    booking_fees.setText("$" + response.body().getData().getDetails().getBookingFee().toString());
                    sub_totals.setText("$" + response.body().getData().getDetails().getBookingrate());


                    if (promo_Apply == 1) {
                        if (pro_dis_type == 1) {//Percentage

                            discounts.setText(pro_discount + "%");
                            double res = (response.body().getData().getDetails().getAmount() / 100.0f) * pro_discount;
                            Log.e("AMOUNT_per", res + "");
                            total = (Integer.parseInt((response.body().getData().getDetails().getAmount().toString())) - (Double.valueOf(res).intValue())) +
                                    Integer.parseInt((response.body().getData().getDetails().getBookingFee().toString()));

                        } else if (pro_dis_type == 2) {//Flat

                            discounts.setText(pro_discount + "$");
                            double res = ((Integer.valueOf(response.body().getData().getDetails().getAmount().toString())) - pro_discount);
                            Log.e("AMOUNT_Flay", res + "");
                            total = (Double.valueOf(res).intValue()) +
                                    Integer.parseInt((response.body().getData().getDetails().getBookingFee().toString()));
                        } else {
                        }

                    } else {
                        discounts.setText(response.body().getData().getDetails().getDiscount().toString());
                        total = ((Integer.valueOf(response.body().getData().getDetails().getAmount().toString()) +
                                Integer.parseInt((response.body().getData().getDetails().getBookingFee().toString())))
                        );
                    }

                    detailsTotalCostTwo.setText("$" + total);

                } else if (response.body().getStatus() == 401) {
                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(act, LoginActivity.class));
                    act.finish();
                } else {
                    if (loader != null && loader.isShowing() && !isFinishing()) {
                        loader.dismiss();
                    }
                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartDetailRes> call, Throwable t) {
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                if (loader != null && loader.isShowing() && !isFinishing()) {
                    loader.dismiss();
                }
            }
        });
    }

    private void Call_Booking() {
        BookingParams addBookingParams = new BookingParams();
        ArrayList<BookingList> bookingLists = new ArrayList<>();
        addToCartRes = new Gson().fromJson(PreferenceUtil.getPref(act).getString(Constants.pref_Cart_Response, ""), AddToCartRes.class);
        cartInfos = addToCartRes.getData().getCartInfo();
        Integer sub_total = 0;
        for (int i = 0; i < cartInfos.size(); i++) {
            sub_total += cartInfos.get(i).getAmount();

            BookingList bookingList = new BookingList();
            bookingList.setCartId(cartInfos.get(i).getCartId());
            bookingList.setBookingFees(5);
            bookingList.setDiscountNo(0);
            bookingList.setPromoCode("");
            bookingLists.add(bookingList);
        }
        addBookingParams.setBookingList(bookingLists);
        addBookingParams.setCustomerId(Integer.parseInt(Config.getPreference(Constants.pref_CustomerId)));
        addBookingParams.setFinalTotal(sub_total+"");
        Log.d("Call_Booking: ","data_passed100----"+ new Gson().toJson(addBookingParams)+"");
        sub_totals.setText("$" + sub_total + "");
        detailsTotalCostTwo.setText("$" + sub_total + "");
        if (loader != null && !loader.isShowing() && !isFinishing()) {
            loader.show();
        }
        Call<AddBookingRes> call = apiService.call_Add_Booking(addBookingParams);
        call.enqueue(new retrofit2.Callback<AddBookingRes>() {
            @Override
            public void onResponse(Call<AddBookingRes> call, retrofit2.Response<AddBookingRes> response) {
                if (response.code()==200) {
                    if (loader != null && loader.isShowing() && !isFinishing()) {
                        loader.dismiss();
                    }
                    PreferenceUtil.getPref(act).edit()
                            .putString(Constants.pref_Cart_Response, "")
                            .apply();

                    CongratulationsDialog(act,"CONGRATULATIONS!","Booking is now pending. We will notify you when the booking is confirmed from the business.");
                } else {
                    if (loader != null && loader.isShowing() && !isFinishing()) {
                        loader.dismiss();
                    }
                    Toast.makeText(act, response.body().getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddBookingRes> call, Throwable t) {
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
                if (loader != null && loader.isShowing() && !isFinishing()) {
                    loader.dismiss();
                }
            }
        });


    }

    public void CongratulationsDialog(final Context context, String title, String txt_detailss) {
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.setContentView(R.layout.dailog_delete);
        TextView txt_title = (TextView) dialog.findViewById(R.id.txt_title);
        TextView txt_done = (TextView) dialog.findViewById(R.id.txt_done);
        TextView txt_details_invite = (TextView) dialog.findViewById(R.id.txt_details_invite);
        TextView txt_details = (TextView) dialog.findViewById(R.id.txt_details);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        txt_title.setText(title);
        txt_details.setText(txt_detailss);
        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("pendingbooking", "pendingbooking");
                context.startActivity(i);
                finish();
            }
        });

        dialog.show();
    }

    private boolean validateFields() {

        if (TextUtils.isEmpty(detailsMadterNumber.getText().toString())) {
            detailsMadterNumber.requestFocus();
            Toast.makeText(act, "Enter Card Detail", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    //Stripes

    public void getCardData(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        if (loader != null && !loader.isShowing() && !isFinishing()) {
            loader.show();
        }
        Card card = new Card(
                cardNumber,
                cardExpMonth,
                cardExpYear,
                cardCVC
        );
        if (validateCard(card)) {
            dialog.dismiss();
            createStripeToken(card);
        } else {
            if (loader != null && loader.isShowing() && !isFinishing()) {
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
                        sendCardToInfoToStripe(token.getId());
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        if (loader != null && loader.isShowing() && !isFinishing()) {
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

    private void sendCardToInfoToStripe(final String cardToken) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    com.stripe.Stripe.apiKey = Utils.secret_key;
                    Map<String, Object> params = new HashMap<>();
                    params.put("amount", Integer.parseInt(total + "00"));

                    params.put("currency", "usd");
                    //params.put("description", "Test charge");
                    params.put("source", cardToken);
                    params.put("receipt_email", Prefs.getString(Constants.pref_CustomerEmail, ""));

                    Charge charge = Charge.create(params);

                    // Toast.makeText(act, charge.getId()+"", Toast.LENGTH_SHORT).show();
                    Log.e("Charge", "run: " + charge.getId());
                    sPayment_token = charge.getId();

                    String cardNumber = edtCard.getText().toString().trim().replaceAll("-", "");
                    final String strLastFourDi = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : "";

                    act.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            loader.dismiss();
                            detailsMadterNumber.setText("XXXX-XXXX-XXXX-" + strLastFourDi);

                        }
                    });
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                } catch (APIConnectionException e) {
                    e.printStackTrace();
                } catch (CardException e) {
                    e.printStackTrace();
                } catch (APIException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void setAlertMessage(String msg) {
        Toast.makeText(act, msg + "", Toast.LENGTH_SHORT).show();
    }

    private void card_entry_dialog() {
        final int count = 0;
        dialog = new android.app.Dialog(act);
        dialog.setContentView(R.layout.cart_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        edtExpiry = dialog.findViewById(R.id.edtExpiry);
        edtCard = dialog.findViewById(R.id.edtCard);
        edtCVV = dialog.findViewById(R.id.edtCVV);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);

        edtExpiry.addTextChangedListener(new TwoDigitsCardTextWatcher(edtExpiry));

        //tv = new CreditCardFormatTextWatcher(edtCard);
        //edtCard.addTextChangedListener(tv);
        //edtCard.addTextChangedListener(new FourDigitCardFormatWatcher());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtCard.getText())) {
                    Toast.makeText(act, "Please enter card number.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtCVV.getText())) {
                    Toast.makeText(act, "Please enter cvv.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(edtExpiry.getText())) {
                    Toast.makeText(act, "Please enter expiry.", Toast.LENGTH_SHORT).show();
                } else {

                    String currentString = edtExpiry.getText().toString();
                    String[] separated = currentString.split("/");
                    getCardData(edtCard.getText().toString().replaceAll("-", ""), Integer.parseInt(separated[0]), Integer.parseInt(separated[1]), edtCVV.getText().toString());
                }
            }
        });

        edtCard.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String[] eachBlock = edtCard.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    edtCard.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((edtCard.getText().length() + 1) % 5) == 0) {

                            if (edtCard.getText().toString().split("-").length <= 3) {
                                edtCard.setText(edtCard.getText() + "-");
                                edtCard.setSelection(edtCard.getText().length());
                            }
                        }
                        a = edtCard.getText().toString();
                    } else {
                        a = edtCard.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    edtCard.setText(a);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        dialog.show();
    }

    public void postToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("code", "S8Q5YE");
            jsonObject.put("code", edtPromoCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ServerCall.getResponse(act, "applyPromocode.json", jsonObject.toString(), true, new ServerCall.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                PromocodeRes postRatingRes = new PromocodeRes();
                postRatingRes = gson.fromJson(result, PromocodeRes.class);

                if (postRatingRes.getStatus() == 200) {
                    promoApply = 1;
                    Toast.makeText(act, postRatingRes.getMessage(), Toast.LENGTH_SHORT).show();
                    pro_discount = postRatingRes.getData().getDetails().getDiscount();
                    pro_dis_type = postRatingRes.getData().getDetails().getDis_type();

                    onResume();
                } else {
                    Toast.makeText(act, postRatingRes.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onError(String error) {
                Toast.makeText(act, error + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
