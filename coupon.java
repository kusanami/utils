import java.util.*;
import java.util.stream.Collectors;


class Product {
    private String code;
    private Float value;

    public Product(String code, Float value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public Float getValue() {
        return value;
    }
}

class Solver {

    private final List<Product> products;
    private Float best;
    private List<Integer> bestAnswer;
    private final Float initialAmount;

    public Solver(Map<String, Float> items, Float amount) {
        this.products = items.entrySet().stream().map(e -> new Product(e.getKey(), e.getValue())).sorted(Comparator.comparing(Product::getValue)).collect(Collectors.toList());
        this.initialAmount = amount;
        this.best = Float.MAX_VALUE;
    }

    public void solve(){
        if(bestAnswer == null){
            solve(0, new LinkedList<>(), initialAmount);
        }
    }

    // Make sure to call solve() before getting this or add guards
    public List<String> getItems(){
        return bestAnswer.stream().map(i -> products.get(i).getCode()).collect(Collectors.toList())
                .stream().sorted().collect(Collectors.toList());
    }

    private void solve(int index, LinkedList<Integer> current, Float money) {

        if (index == products.size() || money.compareTo(products.get(index).getValue()) < 0) {
            check(money, current);
            return;
        }
        Float currentPrice = products.get(index).getValue();

        // We can buy it, so buy it..
        current.add(index);
        solve(index + 1, current, money - currentPrice);
        // and ignore it...
        current.removeLast();
        solve(index + 1, current, money);

    }

    private void check(Float money, LinkedList<Integer> current) {
        if (money.compareTo(best) < 0) {
            // found an better answer
            bestAnswer = new ArrayList<>(current);
            best = money;
        }
    }

    // Make sure to call solve() before getting this or add guards
    public Float getBest() {
        return initialAmount - best;
    }

    public String toString(){
        return String.format("Max expense: %.2f, items: %s", getBest(), getItems());
    }
}

public class Meli {
    public static void main(String[] args) {
        var items = Map.of("MLA1", 100f, "MLA2", 210f, "MLA3", 260f, "MLA4", 80f, "MLA5", 90f);
        Solver case1 = new Solver(items, 500f);
        case1.solve();
        System.out.println(case1);
        // Expects 480, items MLA1, MLA2, MLA4, MLA5

        var moreItems = Map.of("001", 2f, "002",2f ,"003",2f ,"004", 7f);
        Solver case2 = new Solver(moreItems, 7f);
        case2.solve();
        System.out.println(case2);
        // Expects 7, item 004
    }
}
