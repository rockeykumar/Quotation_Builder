package com.example.createpdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "/vision_dataSet.txt";
    private static final String folderName = "vision_services";
    private static final int PERMISSION_REQUEST_CODE = 7;

    Spinner cameraBranDom, cameraBranBullet, cameraDomeMP, cameraBulletMP, channels, HardDiskSpinner;
    String[] cameraBrandList = {"CP Plus", "Dahua", "Hikvision", "None"};
    String[] channelList = {"4 CP Plus", "8 CP Plus", "16 CP Plus", "32 CP Plus", "4 Dahua", "8 Dahua", "16 Dahua", "32 Dahua", "4 Hikvision", "8 Hikvision", "16 Hikvision", "32 Hikvision"};
    String[] megapixelsListDVR  = {"2.4", "1.3", "2", "3", "4", "5", "8"};
    String[] megapixelsListNVR  = {"2", "3", "4", "5", "6", "8"};
    String[] HardDidkList  = {"1", "2", "4"};
    ArrayAdapter<String> cameraBrandAdapterDom, cameraBrandAdapterBullet, cameraMPAdapterDom, cameraMPAdapterBullet, channelAdapter, HardDiskAdapter;

    String strChannel, qty_dvr_nvr, dvr_nvr_price, DOMComapny, DOM_MP, DOM_qty, DOM_Price, BulletCompany, Bullet_MP, Bullet_qty, Bullet_Price;
    String customer_Name, customer_Mobile, customer_Address, Challan_No;
    EditText dvr_nvr_qty, dvr_nvr_rate, qtyDome, priceDome, qtyBullet, priceBullet, custName, mobile, address, offer_rate;
    String fileContent, type_DVR_or_NVR = "DVR", particularsName;
    TextView ChallanNo, sumAmount;

    Button btnSubmit;
    int totalAmount = 0, no_of_cameras = 0, amper = 0, port = 0;
    Bitmap bmpWhatsApp, scaledbmpWhatsApp, visionLogo, visionscaled, offerLogo, offerscaled;
    ArrayList<ArrayList<String>> productList;
    ArrayList<String> list;

    RadioGroup radioGroup;
    RadioButton radiobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar_style);

        HardDiskSpinner = findViewById(R.id.HardDiskSpinner);
        sumAmount = findViewById(R.id.sumAmount);
        offer_rate = findViewById(R.id.offer_rate);
        ChallanNo = findViewById(R.id.ChallanNo);
        dvr_nvr_qty = findViewById(R.id.dvr_nvr_qty);
        dvr_nvr_rate = findViewById(R.id.dvr_nvr_rate);
        channels = findViewById(R.id.Channels);

        cameraBranDom = findViewById(R.id.cameraDomeSpinner);
        cameraDomeMP = findViewById(R.id.cameraDomeMP);
        qtyDome = findViewById(R.id.qtyDome);
        priceDome = findViewById(R.id.priceDome);

        cameraBranBullet = findViewById(R.id.cameraBulletSpinner);
        cameraBulletMP = findViewById(R.id.cameraBulletMP);
        qtyBullet = findViewById(R.id.qtyBullet);
        priceBullet = findViewById(R.id.priceBullet);

        custName = findViewById(R.id.custName);
        customer_Name = custName.getText().toString();

        mobile = findViewById(R.id.mobile);
        customer_Mobile = mobile.getText().toString();

        address = findViewById(R.id.address);
        customer_Address = address.getText().toString();


        HardDiskAdapter = new ArrayAdapter<>(getApplication(), R.layout.my_selected_item, HardDidkList);
        HardDiskAdapter.setDropDownViewResource(R.layout.my_dropdown_list);
        HardDiskSpinner.setAdapter(HardDiskAdapter);

        channelAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, channelList);
        channelAdapter.setDropDownViewResource(R.layout.my_dropdown_list);
        channels.setAdapter(channelAdapter);

        cameraBrandAdapterDom = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, cameraBrandList);
        cameraBrandAdapterDom.setDropDownViewResource(R.layout.my_dropdown_list);
        cameraBranDom.setAdapter(cameraBrandAdapterDom);
        cameraBranDom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = ((TextView)view).getText().toString();
                if(str.equals("None")) {
                    cameraDomeMP.setEnabled(false);
                    qtyDome.setEnabled(false);
                    priceDome.setEnabled(false);
                } else {
                    cameraDomeMP.setEnabled(true);
                    qtyDome.setEnabled(true);
                    priceDome.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cameraBrandAdapterBullet = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, cameraBrandList);
        cameraBrandAdapterBullet.setDropDownViewResource(R.layout.my_dropdown_list);
        cameraBranBullet.setAdapter(cameraBrandAdapterBullet);
        cameraBranBullet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = ((TextView)view).getText().toString();
                if(str.equals("None")) {
                    cameraBulletMP.setEnabled(false);
                    qtyBullet.setEnabled(false);
                    priceDome.setEnabled(false);
                } else {
                    cameraBulletMP.setEnabled(true);
                    qtyBullet.setEnabled(true);
                    priceBullet.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cameraMPAdapterDom = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListDVR);
        cameraMPAdapterDom.setDropDownViewResource(R.layout.my_dropdown_list);
        cameraDomeMP.setAdapter(cameraMPAdapterDom);

        cameraMPAdapterBullet = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListDVR);
        cameraMPAdapterBullet.setDropDownViewResource(R.layout.my_dropdown_list);
        cameraBulletMP.setAdapter(cameraMPAdapterBullet);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radiobtn = findViewById(radioId);
                type_DVR_or_NVR = radiobtn.getText().toString();
                if (type_DVR_or_NVR.equals("DVR")) {
                    String str = channels.getSelectedItem().toString();

                    cameraMPAdapterDom = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListDVR);
                    cameraMPAdapterDom.setDropDownViewResource(R.layout.my_dropdown_list);
                    cameraDomeMP.setAdapter(cameraMPAdapterDom);

                    cameraMPAdapterBullet = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListDVR);
                    cameraMPAdapterBullet.setDropDownViewResource(R.layout.my_dropdown_list);
                    cameraBulletMP.setAdapter(cameraMPAdapterBullet);

                    Toast.makeText(MainActivity.this, "You are Selected : " + type_DVR_or_NVR, Toast.LENGTH_LONG).show();

                } else if (type_DVR_or_NVR.equals("NVR")) {
                    Toast.makeText(MainActivity.this, "You are Selected : " + type_DVR_or_NVR, Toast.LENGTH_LONG).show();

                    cameraMPAdapterDom = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListNVR);
                    cameraMPAdapterDom.setDropDownViewResource(R.layout.my_dropdown_list);
                    cameraDomeMP.setAdapter(cameraMPAdapterDom);

                    cameraMPAdapterBullet = new ArrayAdapter<>(getApplicationContext(), R.layout.my_selected_item, megapixelsListNVR);
                    cameraMPAdapterBullet.setDropDownViewResource(R.layout.my_dropdown_list);
                    cameraBulletMP.setAdapter(cameraMPAdapterBullet);

                    Toast.makeText(MainActivity.this, "You are Selected : " + type_DVR_or_NVR, Toast.LENGTH_LONG).show();

                }
            }
        });


        btnSubmit = findViewById(R.id.submit);
        totalAmount = 0;

        bmpWhatsApp = BitmapFactory.decodeResource(getResources(), R.drawable.small);
        scaledbmpWhatsApp = Bitmap.createScaledBitmap(bmpWhatsApp, 30, 30, false);

        visionLogo = BitmapFactory.decodeResource(getResources(), R.drawable.hdlogo);
        visionscaled = Bitmap.createScaledBitmap(visionLogo, 90, 70, false);


        offerLogo = BitmapFactory.decodeResource(getResources(), R.drawable.offerbackground);
        offerscaled = Bitmap.createScaledBitmap(offerLogo, 235, 129, false);



        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();

        productList = new ArrayList<>();


    }

    private void addItemToCard() {

        list = new ArrayList<>();
        totalAmount = 0;
        no_of_cameras = 0;
        amper = 0;
        port = 0;
        strChannel = channels.getSelectedItem().toString().trim();
        String[] str = strChannel.split(" ");
        String powerSupplyName = "";
        int len = str.length;
        qty_dvr_nvr = dvr_nvr_qty.getText().toString();
        dvr_nvr_price = dvr_nvr_rate.getText().toString();
        if (type_DVR_or_NVR.equals("DVR")) {
            if(len == 3) {
                particularsName = str[0] + " - ch " + type_DVR_or_NVR + " " + str[1] + " " + str[2];
                powerSupplyName = str[1] + " " + str[2];
            } else {
                particularsName = str[0] + " - ch " + type_DVR_or_NVR + " " + str[1];
                powerSupplyName = str[1];
            }
        } else if (type_DVR_or_NVR.equals("NVR")) {
            if(len == 3) {
                particularsName = str[0] + " - ch " + type_DVR_or_NVR + " " + str[1] + " " + str[2];
                powerSupplyName = str[1] + " " + str[2];
            } else {
                particularsName = str[0] + " - ch " + type_DVR_or_NVR + " " + str[1];
                powerSupplyName = str[1];
            }
        }

        
        list.add(particularsName);
        int numQty = Integer.parseInt(qty_dvr_nvr);
        int numPrice = Integer.parseInt(dvr_nvr_price);
        int sum = numQty * numPrice;
        totalAmount += sum;
        list.add(qty_dvr_nvr);
        list.add(dvr_nvr_price);
        list.add(sum + "");
        productList.add(list);

        
        list = new ArrayList<>();
        DOM_MP = cameraDomeMP.getSelectedItem().toString();
        DOMComapny = cameraBranDom.getSelectedItem().toString();
        if (!DOMComapny.equals("None")) {
            DOM_qty = qtyDome.getText().toString();
            DOM_Price = priceDome.getText().toString();
            numQty = Integer.parseInt(DOM_qty);
            numPrice = Integer.parseInt(DOM_Price);
            sum = numQty * numPrice;
            totalAmount += sum;
            no_of_cameras += numQty;
            particularsName = DOM_MP + " MP DOM Camera " + DOMComapny;
            list.add(particularsName);
            list.add(DOM_qty);
            list.add(DOM_Price);
            list.add(sum + "");
            productList.add(list);
        }

        list = new ArrayList<>();
        BulletCompany = cameraBranBullet.getSelectedItem().toString();

        if (!BulletCompany.equals("None")) {
            Bullet_MP = cameraBulletMP.getSelectedItem().toString();
            Bullet_qty = qtyBullet.getText().toString();
            Bullet_Price = priceBullet.getText().toString();
            numQty = Integer.parseInt(Bullet_qty);
            numPrice = Integer.parseInt(Bullet_Price);
            sum = numQty * numPrice;
            no_of_cameras += numQty;
            totalAmount += sum;

            particularsName = DOM_MP + " MP Bullet Camera " + DOMComapny;
            list.add(particularsName);
            list.add(Bullet_qty);
            list.add(Bullet_Price);
            list.add(sum + "");
            productList.add(list);
        }

//        Power Supply
        if (type_DVR_or_NVR.equals("DVR")) {

            if (no_of_cameras <= 5) {
                amper = 5;
                numQty = 1;
                numPrice = 550;
            }
            else if (no_of_cameras > 5 && no_of_cameras <= 10) {
                amper = 10;
                numQty = 1;
                numPrice = 795;
            }
            else if (no_of_cameras > 10 && no_of_cameras < 20) {
                amper = 20;
                numQty = 1;
                numPrice = 1350;
            }
            else if (no_of_cameras >= 20 && no_of_cameras <= 35) {
                amper = 20;
                numQty = 2;
                numPrice = 1350;
            }
            else if (no_of_cameras > 35 && no_of_cameras <= 64) {
                amper = 20;
                numQty = 4;
                numPrice = 1350;
            }

            list = new ArrayList<>();
            list.add("Power Supply 12V - "+ amper + "Amp " + "CP Plus");
            list.add(numQty+"");
            list.add(numPrice+"");
            sum = numQty * numPrice;
            totalAmount += sum;
            list.add(sum+"");
            productList.add(list);
        } else if (type_DVR_or_NVR.equals("NVR")) {

            if (no_of_cameras <= 4) {
                port = 4;
                numQty = 1;
                numPrice = 3100;
                sum = numQty * numPrice;
                totalAmount += sum;
                list = new ArrayList<>();
                list.add("4 - Port POE " + powerSupplyName);
                list.add(numQty + "");
                list.add(numPrice + "");
                list.add(sum+"");
                productList.add(list);

            } else if (no_of_cameras > 4 && no_of_cameras <= 8) {

                port = 8;
                numQty = 1;
                numPrice = 7950;
                list = new ArrayList<>();
                list.add("8 - Port POE " + powerSupplyName);
                list.add(numQty + "");
                list.add(numPrice + "");
                sum = numQty * numPrice;
                list.add(sum+"");
                totalAmount += sum;
                productList.add(list);

            } else {
                int cnt = 0;
                while (no_of_cameras >= 8) {
                    no_of_cameras -= 8;
                    cnt++;
                }
                port = 8;
                numQty = cnt;
                numPrice = 7950;
                list = new ArrayList<>();
                list.add("8 - Port POE " + powerSupplyName);
                list.add(numQty + "");
                list.add(numPrice + "");
                sum = numQty * numPrice;
                totalAmount += sum;
                list.add(sum+"");
                productList.add(list);

                if (no_of_cameras > 4 && no_of_cameras < 8) {
                    numQty = 1;
                    numPrice = 7950;
                    list = new ArrayList<>();
                    list.add("8 - Port POE " + powerSupplyName);
                    list.add(numQty + "");
                    list.add(numPrice + "");
                    sum = numQty * numPrice;
                    totalAmount += sum;
                    list.add(sum+"");
                    productList.add(list);

                } else if (no_of_cameras < 4) {

                    numQty = 1;
                    numPrice = 3100;
                    list = new ArrayList<>();
                    list.add("4 - Port POE " + powerSupplyName);
                    list.add(numQty + "");
                    list.add(numPrice + "");
                    sum = numQty * numPrice;
                    totalAmount += sum;
                    list.add(sum+"");
                    productList.add(list);
                }
            }
        }

//HardDisk
        list = new ArrayList<>();
        String strHardDisk = HardDiskSpinner.getSelectedItem().toString();
        numQty = Integer.parseInt(strHardDisk);
//        numPrice = 3750;
//        sum = numPrice * numQty;
//        if (no_of_cameras <= 8) {
//            numQty = Integer.parseInt(strHardDisk);
//            numPrice = 3750;
//            sum = numPrice * numQty;
//            list.add("Hard Disk "+ numQty +"-TB");
//            list.add(numQty+"");
//            list.add(numPrice+"");
//            list.add("3750");
//            totalAmount += 3750;
//        } else if (no_of_cameras > 8 && no_of_cameras <= 16) {
//            list.add("Hard Disk 2-TB (Approx)");
//            list.add("1");
//            list.add("6000");
//            list.add("6000");
//            totalAmount += 6000;
//        } else if (no_of_cameras > 16 && no_of_cameras <= 32) {
//            list.add("Hard Disk 4-TB (Approx)");
//            list.add("1");
//            list.add("12500");
//            list.add("12500");
//            totalAmount += 12500;
//        }

        if(numQty == 1) {
            numPrice = 3750;
            sum = numPrice;
        } else if(numQty == 2) {
            numPrice = 6723;
            sum = numPrice;
        } else if(numQty == 4) {
            numPrice = 11320;
            sum = numPrice;
        }

        list.add("Hard Disk "+ numQty +"-TB");
        list.add("1");
        list.add(numPrice+"");
        list.add(sum+"");
        totalAmount += sum;
        productList.add(list);

//        Cable
        list = new ArrayList<>();

        if(type_DVR_or_NVR.equals("DVR")) {
            list.add("CCTV Cable CP Plus 90 mtr (as pr required)");
            list.add("1");
            list.add("1250");
            list.add("1250");
            totalAmount += 1250;
            productList.add(list);

        } else if(type_DVR_or_NVR.equals("NVR")) {
            list.add("Cat-6 Cable 100 mtr (as pr required)");
            list.add("1");
            list.add("2750");
            list.add("2750");
            totalAmount += 2750;
            productList.add(list);
        }


//        BNC Connector
        list = new ArrayList<>();
        if(type_DVR_or_NVR.equals("DVR")) {
            list.add("BNC Connector");
            int connector = no_of_cameras * 2;
            sum = connector * 25;
            list.add(connector+"");
            list.add("25");
            list.add(sum+"");
            totalAmount += sum;
            productList.add(list);
        } else if(type_DVR_or_NVR.equals("NVR")) {
            list.add("RJ45 Connector");
            int connector = no_of_cameras * 2 + 4;
            sum = connector * 20;
            list.add(connector+"");
            list.add("20");
            list.add(sum+"");
            totalAmount += sum;
            productList.add(list);
        }

//        DC

        list = new ArrayList<>();
        if(type_DVR_or_NVR.equals("DVR")) {
            list.add("DC Connector");
            int connector = no_of_cameras;
            sum = connector * 20;
            list.add(connector+"");
            list.add("20");
            list.add(sum+"");
            totalAmount += sum;
            productList.add(list);
        }

//        wire clip
        list = new ArrayList<>();
        numQty = no_of_cameras / 2;
        numPrice = 45;
        sum = numQty * numPrice;
        list.add("Wire Clips");
        list.add(numQty+"");
        list.add(numPrice+"");
        list.add(sum+"");
        totalAmount += sum;
        productList.add(list);

//      Installation Charge
        list = new ArrayList<>();
        if(type_DVR_or_NVR.equals("DVR")) {
            numQty = no_of_cameras;
            numPrice = 350;
            sum = numQty * numPrice;
            list.add("Installation");
            list.add(numQty+"");
            list.add((numPrice+""));
            list.add(sum+"");
            totalAmount += sum;
            productList.add(list);

        } else if(type_DVR_or_NVR.equals("NVR")) {
            numQty = no_of_cameras;
            numPrice = 650;
            sum = numQty * numPrice;
            list.add("Installation");
            list.add(numQty+"");
            list.add((numPrice+""));
            list.add(sum+"");
            totalAmount += sum;
            productList.add(list);
        }
    }

    private void createPDF() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (dvr_nvr_qty.getText().toString().length() == 0 ||
                        dvr_nvr_rate.getText().toString().length() == 0 || (!cameraBranDom.getSelectedItem().toString().equals("None") && (qtyDome.getText().toString().length() == 0 ||
                        priceDome.getText().toString().length() == 0)) || (!cameraBranBullet.getSelectedItem().toString().equals("None") && (qtyBullet.getText().toString().length() == 0 ||
                        priceBullet.getText().toString().length() == 0)) || custName.getText().toString().length() == 0 ||
                        address.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Some Fields Empty...!", Toast.LENGTH_LONG).show();

                } else if(mobile.getText().toString().length() != 10) {
                    Toast.makeText(MainActivity.this, "Invalid Mobile Number...!", Toast.LENGTH_LONG).show();
                } else {
                    customer_Name = custName.getText().toString();
                    customer_Mobile = mobile.getText().toString();
                    customer_Address = address.getText().toString();

//                    Toast.makeText(MainActivity.this, "save to error 0", Toast.LENGTH_LONG).show();

                    Challan_No = saveChallanNumber();

//                    Toast.makeText(MainActivity.this, "save to error 01", Toast.LENGTH_LONG).show();

                    ChallanNo.setText(Challan_No);

//                    Toast.makeText(MainActivity.this, "save to error 001", Toast.LENGTH_LONG).show();
                    addItemToCard();

//                    Toast.makeText(MainActivity.this, "save to error 1", Toast.LENGTH_LONG).show();

                    PdfDocument myPdfDocument = new PdfDocument();

                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    Paint whatsAppPaint = new Paint();
                    canvas.drawBitmap(scaledbmpWhatsApp, myPageInfo1.getPageWidth() - 168, 20, whatsAppPaint);

                    Paint mobPaint = new Paint();
                    mobPaint.setTextAlign(Paint.Align.RIGHT);
                    mobPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    mobPaint.setTextSize(18);
                    mobPaint.setTextScaleX(1f);
                    canvas.drawText("7677033044", myPageInfo1.getPageWidth() - 20, 40, mobPaint);
                    canvas.drawText("8102721976", myPageInfo1.getPageWidth() - 20, 70, mobPaint);


                    Paint visionPaint = new Paint();
                    canvas.drawBitmap(visionscaled, 70, 70, visionPaint);


                    Paint headingPaint = new Paint();
                    headingPaint.setTextAlign(Paint.Align.RIGHT);
                    headingPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    headingPaint.setTextSize(34f);
                    headingPaint.setTextScaleX(1.5f);
                    canvas.drawText("Vision Services", myPageInfo1.getPageWidth() - 70, 140, headingPaint);

                    Paint addressPaint = new Paint();
                    addressPaint.setTextAlign(Paint.Align.CENTER);
                    addressPaint.setTextSize(12f);
                    addressPaint.setTextScaleX(1.5f);
                    canvas.drawText("Nabab Bhadur Road, Gulzarbagh, Patna - 800008 (Bihar)", myPageInfo1.getPageWidth() / 2, 160, addressPaint);

                    Paint line = new Paint();
                    line.setColor(Color.BLACK);
                    canvas.drawRect(20, 164, myPageInfo1.getPageWidth() - 20, 166, line);

                    Paint detailPaint = new Paint();

                    detailPaint.setTextAlign(Paint.Align.CENTER);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("(Quotation)", myPageInfo1.getPageWidth() / 2, 185, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.LEFT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("Quotation No. : " + Challan_No, 20, 220, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.RIGHT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("Date : " + getTodaysDate(), myPageInfo1.getPageWidth() - 20, 220, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.LEFT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("Name : " + customer_Name, 20, 250, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.RIGHT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("Mob : " + customer_Mobile, myPageInfo1.getPageWidth() - 20, 250, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.LEFT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("Address : ", 20, 280, detailPaint);

                    detailPaint.setTextAlign(Paint.Align.LEFT);
                    detailPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    detailPaint.setTextSize(12);
                    detailPaint.setTextScaleX(1.5f);
                    canvas.drawText("" + customer_Address, 110, 280, detailPaint);

                    // Rectangle Box
//                    Toast.makeText(MainActivity.this, "save to error 2", Toast.LENGTH_LONG).show();

                    Paint strockRectangle = new Paint();
                    strockRectangle.setStyle(Paint.Style.STROKE);
                    strockRectangle.setStrokeWidth(2);
                    strockRectangle.setColor(Color.BLACK);
                    canvas.drawRect(20, 319, myPageInfo1.getPageWidth() - 20, 706, strockRectangle);

                    strockRectangle.setStyle(Paint.Style.STROKE);
                    strockRectangle.setStrokeWidth(2);
                    strockRectangle.setColor(Color.BLACK);
                    canvas.drawRect(20, 350, myPageInfo1.getPageWidth() - 20, 675, strockRectangle);

                    strockRectangle.setStyle(Paint.Style.STROKE);
                    strockRectangle.setStrokeWidth(2);
                    strockRectangle.setColor(Color.BLACK);
                    canvas.drawRect(60, 319, 370, 675, strockRectangle);


                    canvas.drawLine(420, 319, 422, 675, strockRectangle);
                    canvas.drawLine(475, 319, 477, 706, strockRectangle);

                    // End Rectangle Box

//                    Toast.makeText(MainActivity.this, "save to error 2", Toast.LENGTH_LONG).show();

                    // Title Heading

                    Paint titlePaint = new Paint();
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(9f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("S.No", 41, 338, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(9f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Particulars", 215, 338, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(9f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Qty.", 396, 338, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(9f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Unit", 448, 338, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(10f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Amount", 526, 338, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.RIGHT);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(12f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Total Amount", 470, 695, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.RIGHT);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(12f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("" + totalAmount, myPageInfo1.getPageWidth() - 35, 695, titlePaint);

                    titlePaint.setTextAlign(Paint.Align.LEFT);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(10f);
                    titlePaint.setTextScaleX(1.5f);
                    canvas.drawText("Terms & Conditions", 20, 720, titlePaint);

                    Paint footer = new Paint();
                    footer.setTextSize(10f);
                    footer.setTextScaleX(1.5f);
                    canvas.drawText("1. Goods once sold will not be taken back.", 20, 735, footer);
                    canvas.drawText("2. We are not Responsible for Gurantee & the coverage of", 20, 750, footer);
                    canvas.drawText("Gurantee / Waranty in company Terms & Condition.", 20, 765, footer);

                    Paint sign = new Paint();
                    sign.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Signature", myPageInfo1.getPageWidth() - 35, myPageInfo1.getPageHeight() - 35, sign);


                    // End Title Heading
//                    Toast.makeText(MainActivity.this, "save to error 3", Toast.LENGTH_LONG).show();

                    //    < item 1
                    Paint particular = new Paint();
                    int j = 0;
                    int rightDistance = myPageInfo1.getPageWidth();
                    for (int i = 0; i < productList.size(); i++, j += 25) {

                        ArrayList<String> list = productList.get(i);

                        particular.setTextAlign(Paint.Align.LEFT);
                        particular.setTextSize(13f);
                        detailPaint.setTextScaleX(1.5f);
                        canvas.drawText((i + 1) + "", 37, 372 + j, particular);

                        particular.setTextSize(13f);
                        particular.setTextScaleX(1f);
                        canvas.drawText(list.get(0), 73, 372 + j, particular);

                        particular.setTextSize(13f);
                        detailPaint.setTextScaleX(1.5f);
                        particular.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(list.get(1), rightDistance - 185, 372 + j, particular);

                        particular.setTextSize(13f);
                        detailPaint.setTextScaleX(1.5f);
                        particular.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(list.get(2), rightDistance - 130, 372 + j, particular);


                        particular.setTextSize(13f);
                        detailPaint.setTextScaleX(1.5f);
                        particular.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(list.get(3), rightDistance - 35, 372 + j, particular);
                        particular.setTextAlign(Paint.Align.LEFT);

                    }
                    //    item 1 end
//                    Toast.makeText(MainActivity.this, "save to error 4", Toast.LENGTH_LONG).show();

                    String offerRate = offer_rate.getText().toString();
                    if(offerRate.length() != 0 && Integer.parseInt(offerRate) > 0) {
                        int rate = Integer.parseInt(offerRate);
                        Paint offerPaint = new Paint();
                        canvas.rotate(-25, 180, 580);
                        canvas.drawBitmap(offerscaled, 130, 580, offerPaint);
                        offerPaint.setTextAlign(Paint.Align.CENTER);
                        offerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        offerPaint.setTextSize(15f);
                        offerPaint.setColor(Color.WHITE);
                        offerPaint.setTextScaleX(1.5f);
                        canvas.drawText("Offer Price", 245, 640, offerPaint);
                        offerPaint.setTextSize(18f);
                        canvas.drawText("\u20B9 " + rate, 250, 663, offerPaint);
                    }

//                    Toast.makeText(MainActivity.this, "save to error 5", Toast.LENGTH_LONG).show();
                    myPdfDocument.finishPage(myPage1);
                    File file = new File(Environment.getExternalStorageDirectory(), "/vision_services/" + Challan_No + "_" + customer_Name + ".pdf");

//                    Toast.makeText(MainActivity.this, "save to error 6", Toast.LENGTH_LONG).show();

                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        productList.clear();
                        totalAmount = 0;
                        openDailog(Challan_No + "_" + customer_Name);

//                        Toast.makeText(MainActivity.this, "save to error 7", Toast.LENGTH_LONG).show();
//                        Toast.makeText(MainActivity.this, "Saved to " + "/vision_services/" + Challan_No + "_" + customer_Name + ".pdf", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this, "save to error 8", Toast.LENGTH_LONG).show();
                    }

                    myPdfDocument.close();
//                    Toast.makeText(MainActivity.this, "save to error 9", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String saveChallanNumber() {
        fileContent = getNumber();
//        System.out.println("=================== step 1 ===========================");
//        Toast.makeText(this, "=================== step 1 ===========================", Toast.LENGTH_LONG).show();
        int num = Integer.parseInt(fileContent);
        num = num + 1;
        fileContent = num + "";
//        System.out.println("=================== step 2 ===========================");
//        Toast.makeText(this, "=================== step 2 ===========================", Toast.LENGTH_LONG).show();
        if (!fileContent.equals("")) {
//            System.out.println("=================== step 3 ===========================");
//            Toast.makeText(this, "=================== step 3 ===========================", Toast.LENGTH_LONG).show();
            File myExternalFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
//            System.out.println("=================== step 4 ==========================");
//            Toast.makeText(this, "=================== step 4 ===========================", Toast.LENGTH_LONG).show();
            FileOutputStream fos = null;
//            System.out.println("=================== step 5 ===========================");
//            Toast.makeText(this, "=================== step 5 ===========================", Toast.LENGTH_LONG).show();
            try {
//                System.out.println("=================== step 6 ===========================");
//                Toast.makeText(this, "=================== step 6 ===========================", Toast.LENGTH_LONG).show();
                fos = new FileOutputStream(myExternalFile);
//                System.out.println("=================== step 7 ===========================");
//                Toast.makeText(this, "=================== step 7 ===========================", Toast.LENGTH_LONG).show();
                fos.write(fileContent.getBytes());
//                System.out.println("=================== step 8 ===========================");
//                Toast.makeText(this, "=================== step 8 ===========================", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
//                System.out.println("=================== step 9 ===========================");
//                Toast.makeText(this, "=================== step 9 ===========================", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
//                System.out.println("=================== step 10 ===========================");
//                Toast.makeText(this, "=================== step 10 ===========================", Toast.LENGTH_LONG).show();

            } finally {
                if (fos != null) {
                    try {
                        fos.close();
//                        Toast.makeText(this, "=================== step 11 ===========================", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        Toast.makeText(this, "=================== step 12 ===========================", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {

            Toast.makeText(this, "Input Field Can't be Empty", Toast.LENGTH_LONG).show();
        }


        return fileContent;
    }

    private String getNumber() {

        FileReader fr = null;
        File myExternalFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        if(!myExternalFile.exists()) {
            createFolder();
            return "1989";
        }

        StringBuilder stringBuilder = new StringBuilder();

        try {
            fr = new FileReader(myExternalFile);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while(line != null) {
                stringBuilder.append(line);
                line = br.readLine();
            }

            String res = stringBuilder.toString();
            return res;
        } catch (FileNotFoundException e) {
            return "1989";
        } catch (IOException e) {
            return "1989";
        } finally {
            if(fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    return "1989";
                }
            }
        }
    }

    private void createFolder() {

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            createDirectory(folderName);
        } else {
            askPermission();
//            Toast.makeText(MainActivity.this, "ask permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createDirectory(folderName);
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createDirectory(String folderName) {
        File file = new File(Environment.getExternalStorageDirectory(), folderName);

        if(!file.exists()) {
            file.mkdir();
        }
    }

    public void check(View v) {

        if (dvr_nvr_qty.getText().toString().length() == 0 ||
                dvr_nvr_rate.getText().toString().length() == 0 || (!cameraBranDom.getSelectedItem().toString().equals("None") && (qtyDome.getText().toString().length() == 0 ||
                priceDome.getText().toString().length() == 0)) || (!cameraBranBullet.getSelectedItem().toString().equals("None") && (qtyBullet.getText().toString().length() == 0 ||
                priceBullet.getText().toString().length() == 0)) || custName.getText().toString().length() == 0 ||
                address.getText().toString().length() == 0) {
            Toast.makeText(MainActivity.this, "Some Fields Empty...!", Toast.LENGTH_LONG).show();

        } else if(mobile.getText().toString().length() != 10) {
            Toast.makeText(MainActivity.this, "Invalid Mobile Number...!", Toast.LENGTH_LONG).show();
        } else {
            totalAmount = 0;
            no_of_cameras = 0;
            amper = 0;
            port = 0;
            strChannel = channels.getSelectedItem().toString().trim();

            qty_dvr_nvr = dvr_nvr_qty.getText().toString();
            dvr_nvr_price = dvr_nvr_rate.getText().toString();

            int numQty = Integer.parseInt(qty_dvr_nvr);
            int numPrice = Integer.parseInt(dvr_nvr_price);
            int sum = numQty * numPrice;
            totalAmount += sum;

            DOM_MP = cameraDomeMP.getSelectedItem().toString();
            DOMComapny = cameraBranDom.getSelectedItem().toString();
            if (!DOMComapny.equals("None")) {
                DOM_qty = qtyDome.getText().toString();
                DOM_Price = priceDome.getText().toString();
                numQty = Integer.parseInt(DOM_qty);
                numPrice = Integer.parseInt(DOM_Price);
                sum = numQty * numPrice;
                totalAmount += sum;
                no_of_cameras += numQty;
                particularsName = DOM_MP + " MP DOM Camera " + DOMComapny;
            }

            BulletCompany = cameraBranBullet.getSelectedItem().toString();

            if (!BulletCompany.equals("None")) {
                Bullet_MP = cameraBulletMP.getSelectedItem().toString();
                Bullet_qty = qtyBullet.getText().toString();
                Bullet_Price = priceBullet.getText().toString();
                numQty = Integer.parseInt(Bullet_qty);
                numPrice = Integer.parseInt(Bullet_Price);
                sum = numQty * numPrice;
                no_of_cameras += numQty;
                totalAmount += sum;

                particularsName = DOM_MP + " MP Bullet Camera " + DOMComapny;
            }

            //        Power Supply
            if (type_DVR_or_NVR.equals("DVR")) {

                if (no_of_cameras <= 5) {
                    amper = 5;
                    numQty = 1;
                    numPrice = 550;
                } else if (no_of_cameras > 5 && no_of_cameras <= 10) {
                    amper = 10;
                    numQty = 1;
                    numPrice = 795;
                } else if (no_of_cameras > 10 && no_of_cameras < 20) {
                    amper = 20;
                    numQty = 1;
                    numPrice = 1350;
                } else if (no_of_cameras >= 20 && no_of_cameras <= 35) {
                    amper = 20;
                    numQty = 2;
                    numPrice = 1350;
                } else if (no_of_cameras > 35 && no_of_cameras <= 64) {
                    amper = 20;
                    numQty = 4;
                    numPrice = 1350;
                }

                sum = numQty * numPrice;
                totalAmount += sum;
            } else if (type_DVR_or_NVR.equals("NVR")) {

                if (no_of_cameras <= 4) {
                    port = 4;
                    numQty = 1;
                    numPrice = 3100;
                    sum = numQty * numPrice;
                    totalAmount += sum;

                } else if (no_of_cameras > 4 && no_of_cameras <= 8) {

                    port = 8;
                    numQty = 1;
                    numPrice = 7950;
                    sum = numQty * numPrice;

                    totalAmount += sum;

                } else {
                    int cnt = 0;
                    while (no_of_cameras >= 8) {
                        no_of_cameras -= 8;
                        cnt++;
                    }
                    port = 8;
                    numQty = cnt;
                    numPrice = 7950;
                    sum = numQty * numPrice;
                    totalAmount += sum;

                    if (no_of_cameras > 4 && no_of_cameras < 8) {
                        numQty = 1;
                        numPrice = 7950;
                        sum = numQty * numPrice;
                        totalAmount += sum;

                    } else if (no_of_cameras < 4) {

                        numQty = 1;
                        numPrice = 3100;
                        sum = numQty * numPrice;
                        totalAmount += sum;
                    }
                }
            }

            //HardDisk

            String strHardDisk = HardDiskSpinner.getSelectedItem().toString();
            numQty = Integer.parseInt(strHardDisk);
            if(numQty == 1) {
                numPrice = 3750;
                sum = numPrice;
            } else if(numQty == 2) {
                numPrice = 6723;
                sum = numPrice;
            } else if(numQty == 4) {
                numPrice = 11320;
                sum = numPrice;
            }
            totalAmount += sum;

            //        Cable


            if (type_DVR_or_NVR.equals("DVR")) {
                totalAmount += 1250;

            } else if (type_DVR_or_NVR.equals("NVR")) {
                totalAmount += 2750;
            }


            //        BNC Connector
            if (type_DVR_or_NVR.equals("DVR")) {
                int connector = no_of_cameras * 2;
                sum = connector * 25;
                totalAmount += sum;

            } else if (type_DVR_or_NVR.equals("NVR")) {

                int connector = no_of_cameras * 2 + 4;
                sum = connector * 20;
                totalAmount += sum;
            }

            //        DC

            if (type_DVR_or_NVR.equals("DVR")) {
                int connector = no_of_cameras;
                sum = connector * 20;
                totalAmount += sum;
            }

            //        wire clip
            numQty = no_of_cameras / 2;
            numPrice = 45;
            sum = numQty * numPrice;
            totalAmount += sum;

            //      Installation Charge
            if (type_DVR_or_NVR.equals("DVR")) {
                numQty = no_of_cameras;
                numPrice = 350;
                sum = numQty * numPrice;
                totalAmount += sum;

            } else if (type_DVR_or_NVR.equals("NVR")) {
                numQty = no_of_cameras;
                numPrice = 650;
                sum = numQty * numPrice;
                totalAmount += sum;
            }

            sumAmount.setText(totalAmount + "");
            Toast.makeText(MainActivity.this, "Total Amount : " + totalAmount, Toast.LENGTH_LONG).show();
        }
    }

    private String getTodaysDate(){
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    private void openDailog(String msg) {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Download Successfully...!")
                .setContentText("/storage/emulated/0/vision_services/"+ msg +".pdf")
                .show();
    }
}
