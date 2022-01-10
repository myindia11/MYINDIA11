package com.example.india11.EmailClients;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;
    private static final String SMTP_HOST_NAME = "smtpout.secureserver.net"; //smtp URL
    private static final int SMTP_HOST_PORT = 465; //port number
    private static String SMTP_AUTH_USER = Utils.EMAIL; //email_id of sender
    private static String SMTP_AUTH_PWD = Utils.PASSWORD; //password of sender email_id
    //Constructor
    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        // mProgressDialog = ProgressDialog.show(mContext,"Sending message", "Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send

        //Show success toast
        //Toast.makeText(mContext,"Message Sent",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        /*props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(mSession);

            //Setting sender address
            mm.setFrom(new InternetAddress(Utils.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            //Adding subject
            mm.setSubject(mSubject);
            //Adding message
            mm.setText(mMessage);
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        /*props.put("mail.host", "smtpout.secureserver.net");
        props.put("mail.port", "465");
        props.put("mail.username", Utils.EMAIL);
        props.put("mail.password", Utils.PASSWORD);
        props.put("mail.protocol", "smtp");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Utils.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            //Adding subject
            mm.setSubject(mSubject);
            //Adding message
            mm.setText(mMessage);
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = null;
        try {
            transport = mailSession.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        MimeMessage message = new MimeMessage(mailSession);

        try {
            message.setSubject(mSubject);
            message.setText(mMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Address[] from = new Address[0];//Your domain email
        try {
            from = InternetAddress.parse("MYINDIA11"+Utils.EMAIL);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        try {
            message.addFrom(from);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail)); //Send email To (Type email ID that you want to send)
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }

        try {
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        try {
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        try {
            transport.close();
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        return null;
    }
}
