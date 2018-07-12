package com.mogo.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.mogo.R;
import com.mogo.view.activities.EventDetail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class Share_Adapter extends RecyclerView.Adapter<Share_Adapter.View_Holder> {

    private Context activity;
    private String[] apps;
    //String link;
    private TypedArray typedArray;
    //String uri;

    public Share_Adapter(Context activity, String[] apps, TypedArray typedArray) {
        this.activity = activity;
        this.apps = apps;
        this.typedArray = typedArray;
    }

    @Override
    public Share_Adapter.View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.shareview, parent, false);
        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(final Share_Adapter.View_Holder holder, int position) {
        holder.textView.setText(apps[position]);
        holder.imageView.setImageResource(typedArray.getResourceId(position, 0));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.getAdapterPosition()) {
                    case 0:
                        SharingToSocialMedia();
                        break;
                    case 1:
                        shareToWhatsApp();
                        break;
                    case 2:
                        shareTwitter();
                        break;
                    case 3:
                        sendEmail();
                        break;
                    case 4:
                        sendMessage();
                        break;
                }
            }
        });
    }

    private void sendMessage() {

        Intent message = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
        message.putExtra("sms_body", "I just found this great event " + EventDetail.eventName + " for a good cause happening on " + EventDetail.date + " --- via SoShi-fy" + "\n" + "Itunes: https://itunes.apple.com/us/appspshi-fy/id1330558568?ls=1&mt=8" + "\n" + "Google Play Store: ");
        activity.startActivity(message);
    }

    private void SharingToSocialMedia() {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(activity);
        shareDialog = new ShareDialog(EventDetail.getInstance());
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Event Detail")
                .setContentDescription(
                        EventDetail.charityUrl)
                .setContentUrl(Uri.parse(EventDetail.imageOfEvent))
                .build();
        shareDialog.show(linkContent);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    private void shareIntagramIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, EventDetail.charityUrl);
        shareIntent.setPackage("com.instagram.android");
        activity.startActivity(shareIntent);
    }


    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        // emailIntent.setType("text/plain");
        emailIntent.setType("image/png");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("http://live.csdevhub.com/mogoapp/ws/uploads/event_images/" + EventDetail.eventImage));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I just found this great event " + EventDetail.eventName + " for a good cause happening on " + EventDetail.date + " --- via SoShi-fy" + "\n" + "Itunes: https://itunes.apple.com/us/appspshi-fy/id1330558568?ls=1&mt=8" + "\n" + "Google Play Store: ");
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        activity.startActivity(emailIntent);
    }


    private void shareTwitter() {
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", urlEncode("I just found this great event " + EventDetail.eventName + " for a good cause happening on " + EventDetail.date + " --- via SoShi-fy" + "\n" + "Itunes: https://itunes.apple.com/us/appspshi-fy/id1330558568?ls=1&mt=8" + "\n" + "Google Play Store: "));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
        List<ResolveInfo> matches = activity.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }
        activity.startActivity(intent);
    }

    private void shareToWhatsApp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "I just found this great event " + EventDetail.eventName + " for a good cause happening on " + EventDetail.date + " --- via SoShi-fy" + "\n" + "Itunes: https://itunes.apple.com/us/appspshi-fy/id1330558568?ls=1&mt=8" + "\n" + "Google Play Store: ");
        try {
            activity.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            //  ToastHelper.MakeShortText("Whatsapp have not been installed.");
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("ggg", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    @Override
    public int getItemCount() {
        return apps.length;
    }

    class View_Holder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout relativeLayout;

        View_Holder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_appname);
            imageView = (ImageView) itemView.findViewById(R.id.socialicons);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.view);
        }
    }
}
