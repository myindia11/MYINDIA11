package com.example.india11.MoneyRelatedLayouts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.india11.Common;
import com.example.india11.R;
import com.example.india11.databinding.FragmentTransactionDetailBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class TransactionDetail extends Fragment {
    private FragmentTransactionDetailBinding binding;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionDetailBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.toolbarabout.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());
        binding.name.setText("Name: "+ Common.profileName);
        binding.email.setText("Email: "+Common.profileEmail);
        binding.mobile.setText("Mobile: "+Common.profileMobile);
        binding.transactionType.setText(Common.trasactionType);
        binding.amount.setText(convertValueInIndianCurrency(Double.parseDouble(Common.amountRequest)));
        binding.date.setText(Common.requestTime);
        binding.tid.setText(Common.refId);
        binding.sendEmail.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@myindia11.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"");
            intent.putExtra(Intent.EXTRA_TEXT,"");
            startActivity(Intent.createChooser(intent, ""));
        });
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(Common.refId, BarcodeFormat.CODE_128,300,70);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.bar.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        binding.share.setOnClickListener(view1 -> {
            shareImage(getScreenShot(binding.layout));
            Toast.makeText(getContext(), "Share receipt", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
    public static Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap= Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
    private void shareImage(Bitmap bitmap){

        try {

            File cachePath = new File(getContext().getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/"+Common.refId+".jpeg"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(getContext().getCacheDir(), "images");
        File newFile = new File(imagePath, Common.refId+".jpeg");
        Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.india11", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,Common.refId);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Share through..."));
        }
    }
    public String convertValueInIndianCurrency(double amount){
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("hi", "IN"));
        return formatter.format(amount);
    }
}