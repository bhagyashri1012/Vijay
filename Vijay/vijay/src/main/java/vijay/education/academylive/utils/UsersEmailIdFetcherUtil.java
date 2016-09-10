package vijay.education.academylive.utils;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
public class UsersEmailIdFetcherUtil {


	/**
	 * This class uses the AccountManager to get the primary email address of the
	 * current user.
	 */

	 public  static String getEmail(Context context) {
	    AccountManager accountManager = AccountManager.get(context); 
	    Account account = getAccount(accountManager);

	    if (account == null) {
	      return null;
	    } else {
	      return account.name;
	    }
	  }
	 public  static String getUserName(Context context) {
		    AccountManager accountManager = AccountManager.get(context); 
		    Account account = getAccount(accountManager);
	        String[] parts = account.name.split("@");
		    if (parts.length > 1)
			   {
			        return parts[0].toString();
			   }else
			   {
			return null;
		  }
	 }
	  private static Account getAccount(AccountManager accountManager) {
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    Account account;
	    if (accounts.length > 0) {
	      account = accounts[0];      
	      
	    } else {
	      account = null;
	    }
	    return account;
	  }
}
