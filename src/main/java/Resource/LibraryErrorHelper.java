package Resource;

public class LibraryErrorHelper {

    public String genericError(Exception error)
    {
        printToLog(error);
        return "The string was empty";
    }//end generic error


    public void httpError(Exception error)
    {
        printToLog(error);
    }//end httpError


    private void printToLog(Exception error)
    {
        error.printStackTrace();
        System.out.println(error.toString());
    }//end print to log

    public void errorWithMessage(Exception error, String message)
    {
        error.printStackTrace();
        System.out.println(error);
        System.out.println(message);
    }


}
