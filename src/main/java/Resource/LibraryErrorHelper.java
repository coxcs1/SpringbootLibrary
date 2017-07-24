package Resource;

public class LibraryErrorHelper {

    public String genericError(Exception error){

        printToLog(error);

        return "An error has occurred";
    }//end generic error

    private void printToLog(Exception error){

        error.printStackTrace();
        System.out.println(error);
    }//end print to log


}
