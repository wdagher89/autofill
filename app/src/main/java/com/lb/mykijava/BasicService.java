package com.lb.mykijava;

import android.app.assist.AssistStructure;
import android.os.Build;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.collection.ArrayMap;

import java.util.List;
import java.util.Map;

import android.app.assist.AssistStructure.ViewNode;

import com.lb.mykijava.data.dao.PasswordsDAO;
import com.lb.mykijava.data.models.PasswordEntry;

import java.util.Map.Entry;

import io.realm.Realm;

@RequiresApi(api = Build.VERSION_CODES.O)
public final class BasicService extends AutofillService {

    private static final String TAG = "BasicService";
    private String idPackage;

    @Override
    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal,
                              FillCallback callback) {
        Log.d(TAG, "onFillRequest()");

        // Find autofillable fields
        AssistStructure structure = getLatestAssistStructure(request);
        Map<String, AutofillId> fields = getAutofillableFields(structure);
        Log.d(TAG, "autofillable fields:" + fields);

        if (fields.isEmpty()) {
            toast("No autofill hints found");
            callback.onSuccess(null);
            return;
        }

        // Create the base response
        FillResponse.Builder response = new FillResponse.Builder();

        // 1.Add the dynamic datasets
        String packageName = getApplicationContext().getPackageName();
        Log.d(TAG, "package name" + packageName);

        PasswordsDAO dao = new PasswordsDAO(Realm.getDefaultInstance());

        List<PasswordEntry> items = dao.findItemsByName(formatIdPackage(idPackage));
        if (items.size() > 0) {
            for (PasswordEntry entry : items) {
                Dataset.Builder dataset = new Dataset.Builder();
                for (Entry<String, AutofillId> field : fields.entrySet()) {
                    String hint = field.getKey();
                    AutofillId id = field.getValue();
                    String value = "";
                    if (hint.equals("username"))
                        value = entry.getUsername();
                    else if (hint.equals("password"))
                        value = entry.getPassword();

                    String displayValue = hint.contains("password") ? "**********" : value;
                    RemoteViews remoteView = newDatasetRemoteView(packageName, displayValue);
                    dataset.setValue(id, AutofillValue.forText(value), remoteView);
                }
                response.addDataset(dataset.build());
            }

            callback.onSuccess(response.build());
        } else callback.onSuccess(null);


    }

    private String[] formatIdPackage(String idPackage) {
        idPackage = idPackage.replace("com.", "");
        return idPackage.split("\\.");
    }

    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {
        Log.d(TAG, "onSaveRequest()");
        toast("Save not supported");
        callback.onSuccess();
    }


    @NonNull
    private Map<String, AutofillId> getAutofillableFields(@NonNull AssistStructure structure) {
        Map<String, AutofillId> fields = new ArrayMap<>();
        int nodes = structure.getWindowNodeCount();
        idPackage= structure.getActivityComponent().getPackageName();
        Log.d(TAG, "idPackage " + idPackage);
        for (int i = 0; i < nodes; i++) {
            ViewNode node = structure.getWindowNodeAt(i).getRootViewNode();
            addFields(fields, node);
        }
        return fields;
    }

    private void addFields(@NonNull Map<String, AutofillId> fields,
                           @NonNull ViewNode node) {


        if (node.getClassName() != null && node.getClassName().contains("EditText")) {

            String idEntry = node.getIdEntry();
            String contentDescription = node.getContentDescription() != null ? node.getContentDescription().toString() : null;
            String description = idEntry != null ? idEntry : contentDescription;
            if (description != null) {
                description = description.toLowerCase();
                if (description.contains("email") || description.contains("username")) {
                    fields.put("username", node.getAutofillId());
                } else if (description.contains("password")) {
                    fields.put("password", node.getAutofillId());
                }
            }
        }


        int childrenSize = node.getChildCount();
        for (int i = 0; i < childrenSize; i++) {
            addFields(fields, node.getChildAt(i));
        }
    }


    @NonNull
    static AssistStructure getLatestAssistStructure(@NonNull FillRequest request) {
        List<FillContext> fillContexts = request.getFillContexts();
        return fillContexts.get(fillContexts.size() - 1).getStructure();
    }

    @NonNull
    static RemoteViews newDatasetRemoteView(@NonNull String packageName,
                                            @NonNull CharSequence text) {
        RemoteViews remoteView =
                new RemoteViews(packageName, R.layout.multidataset_service_list_item);
        remoteView.setTextViewText(R.id.text, text);
        remoteView.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
        return remoteView;
    }

    private void toast(@NonNull CharSequence message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}