import model.Model;
import rl.TestRL;

public class Main {

    public static void main(String[] args) throws Exception{
        TestRL<Model> test = new TestRL<>();
        test.train();
    }
}