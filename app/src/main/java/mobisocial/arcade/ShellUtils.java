package mobisocial.arcade;

import android.content.Context;
import android.service.controls.Control;
import android.widget.Toast;

import com.topjohnwu.superuser.Shell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    public static void SU(final String cmd){


        new Thread(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						Process su = Runtime.getRuntime().exec("su -c " + cmd);
						DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
						outputStream.writeBytes("exit\n");
						outputStream.flush();
						BufferedReader reader = new BufferedReader(
                            new InputStreamReader(su.getInputStream()));
						reader.close();
						su.waitFor();

					} catch (IOException | InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();


    }

	public static void NO(final String cmd){
		Shell.sh(cmd).submit();

	}
}
