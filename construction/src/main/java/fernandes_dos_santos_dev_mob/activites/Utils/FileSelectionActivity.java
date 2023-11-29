package fernandes_dos_santos_dev_mob.activites.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

public class FileSelectionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = new Intent("com.example.fernandes_dos_santos_dev_mob.construction.ACTION_RETURN_FILE");
        Uri uri = getIntent().getData();
        resultIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        resultIntent.setDataAndType(uri, getContentResolver().getType(uri));
        FileSelectionActivity.this.setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
