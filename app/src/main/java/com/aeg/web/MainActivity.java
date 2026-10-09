package com.aeg.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {
    static final int LIME = 0xFFC6F432, BG = 0xFF0D0E0C, CARD = 0xFF171815, INK = 0xFFF1EFE8, MUTE = 0xFF9A978D;
    static final String RECIPIENT = "esmailmandefro@gmail.com";

    String[] tNames = {"Corporate", "Restaurant", "Portfolio", "Online shop",
            "Real estate", "Clinic", "School", "Hotel and travel"};
    int[] tColors = {0xFF2A4BFF, 0xFFE4572E, 0xFF8B5CF6, 0xFF00A896,
            0xFF0F766E, 0xFF0EA5A4, 0xFFF59E0B, 0xFF0369A1};
    String[] pkgs = {"Starter", "Growth", "Scale"};
    int[] prices = {25000, 65000, 145000};

    int selectedTemplate = 0;
    TextView total;
    RadioGroup rg;
    EditText nameField, companyField, emailField, detailsField;
    LinearLayout tplRow;

    int dp(int v) { return (int) (v * getResources().getDisplayMetrics().density); }

    GradientDrawable box(int color, int radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    GradientDrawable boxBorder(int color, int radius, int borderColor, int borderW) {
        GradientDrawable g = box(color, radius);
        g.setStroke(dp(borderW), borderColor);
        return g;
    }

    TextView tv(String s, int sp, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    EditText field(String hint, int type, int lines) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(MUTE);
        e.setTextColor(INK);
        e.setInputType(type);
        e.setMinLines(lines);
        e.setGravity(lines > 1 ? Gravity.TOP : Gravity.CENTER_VERTICAL);
        e.setBackground(box(CARD, 14));
        e.setPadding(dp(14), dp(12), dp(14), dp(12));
        return e;
    }

    void add(LinearLayout root, View v, int topDp) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.topMargin = dp(topDp);
        root.addView(v, p);
    }

    void updateTotal() {
        int i = rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()));
        if (i < 0) i = 0;
        total.setText(String.format("Total: ETB %,d", prices[i]));
    }

    void refreshTemplateCards() {
        for (int i = 0; i < tplRow.getChildCount(); i++) {
            LinearLayout card = (LinearLayout) tplRow.getChildAt(i);
            boolean on = i == selectedTemplate;
            card.setBackground(boxBorder(CARD, 16, on ? LIME : CARD, on ? 2 : 0));
        }
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(BG);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(40), dp(20), dp(30));
        sv.addView(root);

        add(root, tv("AEG Web", 34, INK, true), 0);
        add(root, tv("Websites for business companies. Place your order below.", 15, MUTE, false), 4);

        add(root, tv("Your name", 14, INK, true), 24);
        nameField = field("Jane Doe", InputType.TYPE_CLASS_TEXT, 1);
        add(root, nameField, 6);
        add(root, tv("Company", 14, INK, true), 14);
        companyField = field("Acme Inc.", InputType.TYPE_CLASS_TEXT, 1);
        add(root, companyField, 6);
        add(root, tv("Email", 14, INK, true), 14);
        emailField = field("you@company.com", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT, 1);
        add(root, emailField, 6);

        add(root, tv("Choose a template", 14, INK, true), 20);
        HorizontalScrollView hs = new HorizontalScrollView(this);
        hs.setHorizontalScrollBarEnabled(false);
        tplRow = new LinearLayout(this);
        tplRow.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < tNames.length; i++) {
            final int idx = i;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(dp(10), dp(10), dp(10), dp(10));
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(dp(110), ViewGroup.LayoutParams.WRAP_CONTENT);
            cp.rightMargin = dp(10);
            card.setLayoutParams(cp);

            View swatch = new View(this);
            swatch.setBackground(box(tColors[i], 10));
            LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(50));
            card.addView(swatch, sp);

            TextView label = tv(tNames[i], 13, INK, true);
            label.setPadding(0, dp(8), 0, 0);
            card.addView(label);

            card.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { selectedTemplate = idx; refreshTemplateCards(); }
            });
            tplRow.addView(card);
        }
        hs.addView(tplRow);
        add(root, hs, 10);
        refreshTemplateCards();

        add(root, tv("Package", 14, INK, true), 20);
        rg = new RadioGroup(this);
        for (int i = 0; i < pkgs.length; i++) {
            RadioButton rb = new RadioButton(this);
            rb.setId(View.generateViewId());
            rb.setText(String.format("%s  -  ETB %,d", pkgs[i], prices[i]));
            rb.setTextColor(INK);
            rg.addView(rb);
            if (i == 1) rg.check(rb.getId());
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup g, int id) { updateTotal(); }
        });
        add(root, rg, 4);

        add(root, tv("About your project", 14, INK, true), 14);
        detailsField = field("What does your business do?", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE, 4);
        add(root, detailsField, 6);

        total = tv("", 20, LIME, true);
        add(root, total, 22);
        updateTotal();
        add(root, tv("All payments are made in Ethiopian Birr (ETB).", 12, MUTE, false), 2);

        Button send = new Button(this);
        send.setText("Send order");
        send.setAllCaps(false);
        send.setTextColor(0xFF111210);
        send.setTypeface(Typeface.DEFAULT_BOLD);
        send.setBackground(box(LIME, 30));
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendOrder(); }
        });
        add(root, send, 18);

        setContentView(sv);
    }

    void sendOrder() {
        int pi = rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()));
        if (pi < 0) pi = 0;
        String body = "Name: " + nameField.getText().toString() + "\n"
                + "Company: " + companyField.getText().toString() + "\n"
                + "Email: " + emailField.getText().toString() + "\n"
                + "Template: " + tNames[selectedTemplate] + "\n"
                + "Package: " + pkgs[pi] + " (ETB " + String.format("%,d", prices[pi]) + ")\n\n"
                + "Project details:\n" + detailsField.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{RECIPIENT});
        intent.putExtra(Intent.EXTRA_SUBJECT, "New AEG Web order from " + nameField.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(intent, "Send order via"));
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
