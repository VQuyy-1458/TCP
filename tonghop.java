//TCp.Laptop
package TCP;

import java.io.Serializable;

public class Laptop implements Serializable {
    private static final long serialVersionUID = 20150711L;

    private int id;
    private String code;
    private String name;
    private int quantity;

    public Laptop(int id, String code, String name, int quantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Laptop{id=" + id
                + ", code='" + code + '\''
                + ", name='" + name + '\''
                + ", quantity=" + quantity
                + '}';
    }
}

//TCP.Customer

package TCP;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 20170711L;

    private int id;
    private String code;
    private String name;
    private String dayOfBirth;
    private String userName;

    public Customer(
            int id,
            String code,
            String name,
            String dayOfBirth,
            String userName
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Customer{id=" + id
                + ", code='" + code + '\''
                + ", name='" + name + '\''
                + ", dayOfBirth='" + dayOfBirth + '\''
                + ", userName='" + userName + '\''
                + '}';
    }
}

//client1 
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau:

// a. Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;EC4F899B

// b. Nhận một chuỗi ngẫu nhiên là danh sách các một số tên miền từ server

// Ví dụ: giHgWHwkLf0Rd0.io, I7jpjuRw13D.io, wXf6GP3KP.vn, MdpIzhxDVtTFTF.edu, TUHuMfn25chmw.vn, HHjE9.com, 4hJld2m2yiweto.vn, y2L4SQwH.vn, s2aUrZGdzS.com, 4hXfJe9giAA.edu

// c. Tìm kiếm các tên miền .edu và gửi lên server

// Ví dụ: MdpIzhxDVtTFTF.edu, 4hXfJe9giAA.edu

// d. Đóng kết nối và kết thúc chương trình.
import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B23DCCN947";
        String questionCode = "y0wlSD9L";

        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream()))) {

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + questionCode;

            writer.write(request);
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi: " + request);

            // b. Nhận danh sách domain
            String response = reader.readLine();

            System.out.println("Server gửi:");
            System.out.println(response);

            // c. Tìm các domain .edu
            String[] domains = response.split(",");

            StringBuilder eduDomains = new StringBuilder();

            for (String domain : domains) {
                domain = domain.trim();

                if (domain.endsWith(".edu")) {
                    if (eduDomains.length() > 0) {
                        eduDomains.append(", ");
                    }

                    eduDomains.append(domain);
                }
            }

            // Gửi lại danh sách domain .edu
            writer.write(eduDomains.toString());
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi lại:");
            System.out.println(eduDomains);

            // d. try-with-resources sẽ tự động đóng kết nối

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//client2
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
// Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server trên sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;FF49DC02

// b. Nhận dữ liệu từ server là một chuỗi các giá trị số nguyên được phân tách nhau bởi ký tự ,

// Ví dụ: 1,3,9,19,33,20

// c. Thực hiện tìm giá trị khoảng cách nhỏ nhất của các phần tử nằm trong chuỗi và hai giá trị lớn nhất tạo nên khoảng cách đó. Gửi lên server chuỗi gồm "khoảng cách nhỏ nhất, số thứ nhất, số thứ hai".

// Ví dụ: 1,19,20

// d. Đóng kết nối và kết thúc
public class Client2 {
    public static void main(String[] args) {

        String studentCode = "B23DCCN947";
        String questionCode = "9ZcV3SWl";

        try {
            Socket socket = new Socket("36.50.135.242", 2206);
            socket.setSoTimeout(5000);

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + questionCode;

            System.out.println("Gửi: [" + request + "]");

            output.write(request.getBytes());
            output.flush();

            // b. Nhận chuỗi số
            byte[] buffer = new byte[1024];

            int length = input.read(buffer);

            if (length == -1) {
                System.out.println("Server đã đóng kết nối.");
                return;
            }

            String response = new String(buffer, 0, length).trim();

            System.out.println("Server gửi: [" + response + "]");

            // Tách các số
            String[] parts = response.split(",");

            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            // c. Tìm khoảng cách nhỏ nhất
            int minDistance = Integer.MAX_VALUE;
            int number1 = 0;
            int number2 = 0;

            for (int i = 0; i < numbers.length; i++) {
                for (int j = i + 1; j < numbers.length; j++) {

                    int distance =
                            Math.abs(numbers[i] - numbers[j]);

                    if (distance < minDistance) {
                        minDistance = distance;
                        number1 = numbers[i];
                        number2 = numbers[j];
                    }
                }
            }

            // Đưa số nhỏ hơn lên trước
            if (number1 > number2) {
                int temp = number1;
                number1 = number2;
                number2 = temp;
            }

            String result =
                    minDistance + "," + number1 + "," + number2;

            System.out.println("Kết quả: [" + result + "]");

            // Gửi kết quả
            output.write(result.getBytes());
            output.flush();

            System.out.println("Đã gửi kết quả.");

            // d. Đóng kết nối
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//client3

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2206 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng các luồng byte (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;2B3A6510

// b. Nhận dữ liệu từ server là một chuỗi các giá trị số nguyên được phân tách nhau bởi ký tự ,.

// Ví dụ: 1,3,9,19,33,20

// c. Tìm và gửi lên server giá trị lớn thứ hai cùng vị trí xuất hiện của nó trong chuỗi.Ví dụ: 20,5

// d. Đóng kết nối và kết thúc chương trình.
public class Client3 {
    public static void main(String[] args) {
        String studentCode = "B23DCCN947"; // Thay nếu mã sinh viên của bạn khác
        String questionCode = "1gg78Sl2";

        try (Socket socket = new Socket("36.50.135.242", 2206)) {
            socket.setSoTimeout(5000);

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // a. Gửi mã sinh viên và mã câu hỏi
            String request = studentCode + ";" + questionCode;
            output.write(request.getBytes(StandardCharsets.UTF_8));
            output.flush();

            System.out.println("Da gui: " + request);

            // b. Nhận chuỗi số từ server
            byte[] buffer = new byte[1024];
            int length = input.read(buffer);

            if (length == -1) {
                System.out.println("Server da dong ket noi.");
                return;
            }

            String response = new String(
                    buffer, 0, length, StandardCharsets.UTF_8
            ).trim();

            System.out.println("Server gui: " + response);

            // Chuyển chuỗi thành mảng số nguyên
            String[] parts = response.split(",");
            int[] numbers = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i].trim());
            }

            if (numbers.length < 2) {
                System.out.println("Day so khong co du hai phan tu.");
                return;
            }

            // Tìm giá trị lớn nhất
            int largest = numbers[0];
            for (int number : numbers) {
                if (number > largest) {
                    largest = number;
                }
            }

            // Tìm giá trị lớn thứ hai, phân biệt với giá trị lớn nhất
            boolean foundSecond = false;
            int secondLargest = Integer.MIN_VALUE;

            for (int number : numbers) {
                if (number < largest
                        && (!foundSecond || number > secondLargest)) {
                    secondLargest = number;
                    foundSecond = true;
                }
            }

            if (!foundSecond) {
                System.out.println("Day so khong co gia tri lon thu hai khac nhau.");
                return;
            }

            // Tìm vị trí xuất hiện đầu tiên của giá trị lớn thứ hai.
            // Đề minh họa vị trí tính từ 0: 20 nằm ở chỉ số 5.
            int position = -1;
            for (int i = 0; i < numbers.length; i++) {
                if (numbers[i] == secondLargest) {
                    position = i;
                    break;
                }
            }

            // c. Gửi giá trị lớn thứ hai và vị trí
            String result = secondLargest + "," + position;
            output.write(result.getBytes(StandardCharsets.UTF_8));
            output.flush();

            System.out.println("Da gui ket qua: " + result);

            // d. Socket tự đóng khi kết thúc try
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//client4

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2208 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng byte (BufferedWriter/BufferedReader) theo kịch bản sau:

// a. Gửi một chuỗi gồm mã sinh viên và mã câu hỏi với định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;BAA62945

// b. Nhận một chuỗi ngẫu nhiên từ server

// Ví dụ: dgUOo ch2k22ldsOo

// c. Liệt kê các ký tự (là chữ hoặc số) xuất hiện nhiều hơn một lần trong chuỗi và số lần xuất hiện của chúng và gửi lên server

// Ví dụ: d:2,O:2,o:2,2:3,

// d. Đóng kết nối và kết thúc chương trình.
public class Client4 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2208;

        String studentCode = "B23DCCN947"; // Thay bằng mã sinh viên của bạn
        String questionCode = "lU6i7SwU";   // Kiểm tra lại mã trên trang đề

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            // a. Gửi mã sinh viên;mã câu hỏi
            String request = studentCode + ";" + questionCode;
            writer.write(request);
            writer.newLine();
            writer.flush();

            System.out.println("Da gui: " + request);

            // b. Nhận chuỗi từ server
            String received = reader.readLine();

            if (received == null) {
                System.out.println("Server da dong ket noi ma chua gui du lieu.");
                return;
            }

            System.out.println("Server gui: " + received);

            // c. Đếm chữ cái và chữ số, giữ thứ tự xuất hiện lần đầu
            Map<Character, Integer> counts = new LinkedHashMap<>();

            for (char c : received.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    counts.put(c, counts.getOrDefault(c, 0) + 1);
                }
            }

            StringBuilder result = new StringBuilder();

            for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > 1) {
                    result.append(entry.getKey())
                          .append(":")
                          .append(entry.getValue())
                          .append(",");
                }
            }

            // Gửi kết quả lên server
            writer.write(result.toString());
            writer.newLine();
            writer.flush();

            System.out.println("Da gui ket qua: " + result);

            // Socket tự đóng khi thoát khỏi try

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//client5 

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
// Một chương trình máy chủ cho phép kết nối qua TCP tại cổng 2207 (hỗ trợ thời gian liên lạc tối đa cho mỗi yêu cầu là 5s), yêu cầu xây dựng chương trình (tạm gọi là client) thực hiện kết nối tới server tại cổng 2207, sử dụng luồng byte dữ liệu (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:

// a. Gửi chuỗi là mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;1D25ED92

// b. Nhận lần lượt hai số nguyên a và b từ server

// c. Thực hiện tính toán tổng, tích và gửi lần lượt từng giá trị theo đúng thứ tự trên lên server

// d. Đóng kết nối và kết thúc
public class Client5 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B23DCCN947"; // Thay bằng mã sinh viên của bạn
        String questionCode = "QtFB3UBt";  // Đối chiếu lại với trang đề

        try (Socket socket = new Socket(host, port);
             DataInputStream input = new DataInputStream(
                     socket.getInputStream());
             DataOutputStream output = new DataOutputStream(
                     socket.getOutputStream())) {

            socket.setSoTimeout(5000);

            // a. Gửi mã sinh viên;mã câu hỏi
            String request = studentCode + ";" + questionCode;
            output.writeUTF(request);
            output.flush();

            System.out.println("Da gui: " + request);

            // b. Nhận lần lượt hai số nguyên a và b
            int a = input.readInt();
            int b = input.readInt();

            System.out.println("a = " + a);
            System.out.println("b = " + b);

            // c. Gửi tổng, sau đó gửi tích
            int sum = a + b;
            int product = a * b;

            output.writeInt(sum);
            output.writeInt(product);
            output.flush();

            System.out.println("Da gui tong: " + sum);
            System.out.println("Da gui tich: " + product);

            // d. Socket và các luồng tự đóng khi thoát khỏi try

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//client6

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
// Mật mã caesar, còn gọi là mật mã dịch chuyển, để giải mã thì mỗi ký tự nhận được sẽ được thay thế bằng một ký tự cách nó một đoạn s.

// Ví dụ: với s = 3 thì ký tự A sẽ được thay thế bằng ký tự D

// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng chương trình client tương tác với server trên, sử dụng các luồng byte (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:

// a. Gửi một chuỗi gồm mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;D68C93F7

// b. Nhận lần lượt chuỗi đã bị mã hóa caesar và giá trị dịch chuyển s nguyên

// c. Thực hiện giải mã ra thông điệp ban đầu và gửi lên Server

// d. Đóng kết nối và kết thúc chương trình.
public class Client6 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2207;

        String studentCode = "B23DCCN947"; // Thay nếu cần
        String questionCode = "PrA3JgSl";   // Kiểm tra lại trên trang đề

        try (Socket socket = new Socket(host, port);
             DataInputStream input = new DataInputStream(
                     socket.getInputStream());
             DataOutputStream output = new DataOutputStream(
                     socket.getOutputStream())) {

            socket.setSoTimeout(5000);

            // a. Gửi mã sinh viên;mã câu hỏi
            String request = studentCode + ";" + questionCode;
            output.writeUTF(request);
            output.flush();

            System.out.println("Da gui: " + request);

            // b. Nhận chuỗi Caesar và số dịch chuyển s
            String encrypted = input.readUTF();
            int shift = input.readInt();

            System.out.println("Chuoi ma hoa: " + encrypted);
            System.out.println("Do dich chuyen: " + shift);

            // c. Giải mã Caesar rồi gửi thông điệp ban đầu
            String decoded = decodeCaesar(encrypted, shift);

            output.writeUTF(decoded);
            output.flush();

            System.out.println("Thong diep da giai ma: " + decoded);
            System.out.println("Da gui thong diep len server.");

            // d. Socket và các luồng tự đóng khi thoát khỏi try

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String decodeCaesar(String text, int shift) {
        int normalizedShift = Math.floorMod(shift, 26);
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char decoded = (char) ('A'
                        + Math.floorMod(c - 'A' - normalizedShift, 26));
                result.append(decoded);
            } else if (c >= 'a' && c <= 'z') {
                char decoded = (char) ('a'
                        + Math.floorMod(c - 'a' - normalizedShift, 26));
                result.append(decoded);
            } else {
                // Giữ nguyên dấu cách, số và dấu câu
                result.append(c);
            }
        }

        return result.toString();
    }
}

//client7

import TCP.Laptop;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
// Thông tin sản phẩm vì một lý do nào đó đã bị sửa đổi thành không đúng, cụ thể:

// a) Tên sản phẩm bị đổi ngược từ đầu tiên và từ cuối cùng, ví dụ: “lenovo thinkpad T520” bị chuyển thành “T520 thinkpad lenovo”

// b) Số lượng sản phẩm cũng bị đảo ngược giá trị, ví dụ từ 9981 thành 1899

// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectInputStream / ObjectOutputStream) để gửi/nhận và sửa các thông tin bị sai của sản phẩm. Chi tiết dưới đây:

// a) Đối tượng trao đổi là thể hiện của lớp Laptop được mô tả như sau

// Tên đầy đủ của lớp: TCP.Laptop
// Các thuộc tính: id int, code String, name String, quantity int
// Hàm khởi tạo đầy đủ các thuộc tính được liệt kê ở trên
// Trường dữ liệu: private static final long serialVersionUID = 20150711L;
// b) Tương tác với server theo kịch bản

// 1) Gửi đối tượng là chuỗi chứa mã sinh viên và mã câu hỏi với định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;5AD2B818

// 2) Nhận một đối tượng là thể hiện của lớp Laptop từ server

// 3) Sửa các thông tin sai của sản phẩm về tên và số lượng.

// Gửi đối tượng vừa được sửa sai lên server

// 4) Đóng socket và kết thúc chương trình.
public class Client7 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "B23DCCN947";
        String questionCode = "perSh5Gk";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            // Tạo output trước rồi flush để tránh chờ nhau khi khởi tạo object stream
            ObjectOutputStream output =
                    new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            ObjectInputStream input =
                    new ObjectInputStream(socket.getInputStream());

            // 1. Gửi chuỗi mã sinh viên;mã câu hỏi dưới dạng object
            String request = studentCode + ";" + questionCode;
            output.writeObject(request);
            output.flush();

            System.out.println("Da gui: " + request);

            // 2. Nhận đối tượng Laptop từ server
            Laptop laptop = (Laptop) input.readObject();
            System.out.println("Laptop nhan duoc: " + laptop);

            // 3. Sửa tên: đổi vị trí từ đầu tiên và từ cuối cùng
            laptop.setName(swapFirstAndLastWords(laptop.getName()));

            // Đảo ngược các chữ số của quantity
            laptop.setQuantity(reverseDigits(laptop.getQuantity()));

            // Gửi đối tượng đã sửa lên server
            output.writeObject(laptop);
            output.flush();

            System.out.println("Laptop da gui lai: " + laptop);

            // Socket tự đóng khi kết thúc try
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String swapFirstAndLastWords(String name) {
        String[] words = name.trim().split("\\s+");

        if (words.length < 2) {
            return name;
        }

        String temp = words[0];
        words[0] = words[words.length - 1];
        words[words.length - 1] = temp;

        return String.join(" ", words);
    }

    private static int reverseDigits(int number) {
        String reversed = new StringBuilder(
                Integer.toString(number)
        ).reverse().toString();

        return Integer.parseInt(reversed);
    }
}

//client8

import TCP.Customer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
// Thông tin khách hàng cần thay đổi định dạng lại cho phù hợp với khu vực, cụ thể:

// a. Tên khách hàng cần được chuẩn hóa theo định dạng mới.

// Ví dụ: nguyen van hai duong -> DUONG, Nguyen Van Hai

// b. Ngày sinh của khách hàng hiện đang ở dạng mm-dd-yyyy, cần được chuyển thành định dạng dd/mm/yyyy.

// Ví dụ: 10-11-2012 -> 11/10/2012

// c. Tài khoản khách hàng là các chữ cái in thường được sinh tự động từ họ tên khách hàng.

// Ví dụ: nguyen van hai duong -> nvhduong

// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2209 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác với server sử dụng các luồng đối tượng (ObjectInputStream / ObjectOutputStream) thực hiện gửi/nhận đối tượng khách hàng và chuẩn hóa. Cụ thể:

// a. Đối tượng trao đổi là thể hiện của lớp Customer được mô tả như sau

// Tên đầy đủ của lớp: TCP.Customer
// Các thuộc tính: id int, code String, name String, dayOfBirth String, userName String
// Hàm khởi tạo đầy đủ các thuộc tính được liệt kê ở trên
// Trường dữ liệu: private static final long serialVersionUID = 20170711L;
// b. Tương tác với server theo kịch bản dưới đây:

// 1) Gửi đối tượng là một chuỗi gồm mã sinh viên và mã câu hỏi ở định dạng studentCode;qCode.

// Ví dụ: B15DCCN999;F2DA54F3

// 2) Nhận một đối tượng là thể hiện của lớp Customer từ server với các thông tin đã được thiết lập

// 3) Thay đổi định dạng theo các yêu cầu ở trên và gán vào các thuộc tính tương ứng.

// Gửi đối tượng đã được sửa đổi lên server

// 4) Đóng socket và kết thúc chương trình.
public class Client8 {
    public static void main(String[] args) {
        String host = "36.50.135.242";
        int port = 2209;

        String studentCode = "B23DCCN947";
        String questionCode = "nhO0zIQp"; // Đối chiếu lại với trang đề

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            // Tạo và flush output trước để tránh hai phía cùng chờ header stream
            ObjectOutputStream output =
                    new ObjectOutputStream(socket.getOutputStream());
            output.flush();

            ObjectInputStream input =
                    new ObjectInputStream(socket.getInputStream());

            // 1. Gửi object String: mã sinh viên;mã câu hỏi
            String request = studentCode + ";" + questionCode;
            output.writeObject(request);
            output.flush();

            System.out.println("Da gui: " + request);

            // 2. Nhận đối tượng Customer
            Customer customer = (Customer) input.readObject();
            System.out.println("Customer nhan duoc: " + customer);

            // 3a. Chuẩn hóa tên:
            // các từ trước tên cuối viết hoa chữ cái đầu;
            // từ cuối viết in hoa và đưa lên đầu.
            customer.setName(formatName(customer.getName()));

            // 3b. Đổi ngày MM-dd-yyyy thành dd/MM/yyyy
            customer.setDayOfBirth(formatDate(customer.getDayOfBirth()));

            // 3c. Tạo username: chữ cái đầu các từ trước + từ cuối viết thường
            customer.setUserName(makeUsername(customer.getName()));

            // Gửi Customer đã chỉnh sửa lại server
            output.writeObject(customer);
            output.flush();

            System.out.println("Customer da gui lai: " + customer);

            // Socket tự đóng khi kết thúc try
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatName(String name) {
        String[] words = name.trim().toLowerCase(Locale.ROOT).split("\\s+");

        if (words.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        // Tên cuối đưa lên đầu và viết in hoa
        result.append(words[words.length - 1].toUpperCase(Locale.ROOT));
        result.append(", ");

        // Các từ còn lại viết hoa chữ cái đầu
        for (int i = 0; i < words.length - 1; i++) {
            if (i > 0) {
                result.append(" ");
            }

            String word = words[i];
            result.append(Character.toUpperCase(word.charAt(0)));
            result.append(word.substring(1));
        }

        return result.toString();
    }

    private static String formatDate(String date) {
        // Ngày đầu vào: MM-dd-yyyy
        DateTimeFormatter inputFormat =
                DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ROOT);

        // Ngày đầu ra: dd/MM/yyyy
        DateTimeFormatter outputFormat =
                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);

        return LocalDate.parse(date, inputFormat).format(outputFormat);
    }

    private static String makeUsername(String formattedName) {
        // Tên đã có dạng: "DUONG, Nguyen Van Hai"
        String[] sections = formattedName.split(",", 2);

        String surname = sections[0].trim().toLowerCase(Locale.ROOT);
        String givenNames = sections.length > 1
                ? sections[1].trim().toLowerCase(Locale.ROOT)
                : "";

        StringBuilder username = new StringBuilder();

        for (String word : givenNames.split("\\s+")) {
            if (!word.isEmpty()) {
                username.append(word.charAt(0));
            }
        }

        username.append(surname);
        return username.toString();
    }
}

//client9

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2210 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng GZIPInputStream/GZIPOutputStream để trao đổi thông tin (mỗi thông điệp là một dòng text UTF-8 kết thúc bằng ‘\n’ và toàn bộ dữ liệu truyền/nhận đều được nén GZIP), theo thứ tự sau:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;GZLEN01

// b. Nhận dữ liệu từ server, sau khi giải nén là một chuỗi văn bản.

// c. Thực hiện đảo ngược chuỗi nhận được, sau đó mã hóa chuỗi đã đảo ngược sang định dạng Base64. Gửi kết quả lên server theo khuôn dạng: <reversed_string>|<base64_string> Ví dụ: Nhận 123 → Đảo ngược thành 321 → Base64 của 321 là MzIx → Gửi lên: 321|MzIx

// d. Đóng kết nối và kết thúc chương trình.
public class Client9 {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2210;

    private static final String STUDENT_CODE = "B23DCCN947";
    private static final String QUESTION_CODE = "2RKzYioY";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            socket.setSoTimeout(5000);

            // GZIPOutputStream được giữ mở để gửi nhiều dòng trên cùng kết nối.
            GZIPOutputStream gzipOutput =
                    new GZIPOutputStream(socket.getOutputStream(), true);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            gzipOutput,
                            StandardCharsets.UTF_8
                    )
            );

            // a. Gửi mã sinh viên;mã câu hỏi
            String request = STUDENT_CODE + ";" + QUESTION_CODE;
            writer.write(request);
            writer.write('\n');
            writer.flush();

            System.out.println("Da gui: " + request);

            // b. Đọc và giải nén một dòng từ GZIP stream đang mở
            String received = readGzipLine(socket.getInputStream());
            System.out.println("Server gui: " + received);

            // c. Đảo chuỗi, mã hóa Base64 rồi gửi kết quả
            String reversed = new StringBuilder(received).reverse().toString();

            String base64 = Base64.getEncoder().encodeToString(
                    reversed.getBytes(StandardCharsets.UTF_8)
            );

            String result = reversed + "|" + base64;

            writer.write(result);
            writer.write('\n');
            writer.flush();

            System.out.println("Da gui ket qua: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readGzipLine(InputStream input) throws Exception {
        // Đọc header GZIP 10 byte. HEX server gửi bắt đầu bằng:
        // 1F 8B 08 00 00 00 00 00 00 FF
        byte[] header = new byte[10];

        for (int i = 0; i < header.length; i++) {
            int value = input.read();

            if (value == -1) {
                throw new java.io.IOException("Server dong ket noi som.");
            }

            header[i] = (byte) value;
        }

        if ((header[0] & 0xFF) != 0x1F || (header[1] & 0xFF) != 0x8B) {
            throw new java.io.IOException("Du lieu server gui khong co header GZIP.");
        }

        // GZIP chứa dữ liệu DEFLATE thô sau header.
        // nowrap=true bỏ qua việc chờ trailer GZIP, vì server vẫn giữ socket mở.
        Inflater inflater = new Inflater(true);
        StringBuilder line = new StringBuilder();
        byte[] oneCompressedByte = new byte[1];
        byte[] decompressed = new byte[256];

        try {
            while (true) {
                int count = inflater.inflate(decompressed);

                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        char c = (char) (decompressed[i] & 0xFF);

                        if (c == '\n') {
                            return line.toString().trim();
                        }

                        if (c != '\r') {
                            line.append(c);
                        }
                    }
                }

                if (inflater.needsDictionary()) {
                    throw new DataFormatException(
                            "GZIP stream yeu cau dictionary."
                    );
                }

                if (inflater.needsInput()) {
                    int value = input.read();

                    if (value == -1) {
                        throw new java.io.IOException(
                                "Server dong ket noi truoc khi gui het dong."
                        );
                    }

                    oneCompressedByte[0] = (byte) value;
                    inflater.setInput(oneCompressedByte);
                }
            }
        } finally {
            inflater.end();
        }
    }
}

//clent10

import java.io.ByteArrayOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2210 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng GZIPInputStream/GZIPOutputStream để trao đổi thông tin (mỗi thông điệp là một dòng text UTF-8 kết thúc bằng ‘\n’ và toàn bộ dữ liệu truyền/nhận đều được nén GZIP), theo thứ tự sau:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;GZCRC_LEN03

// b. Nhận dữ liệu từ server, sau khi giải nén là một chuỗi văn bản.

// c. Sắp xếp các ký tự trong chuỗi nhận được theo thứ tự từ điển (tăng dần theo mã ASCII). Sau đó gửi chuỗi kết quả đã sắp xếp lên server.

// Ví dụ: Nhận về dbca1 thì gửi lên server 1abcd

// d. Đóng kết nối và kết thúc chương trình.
public class Client10 {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2210;
    private static final int TIMEOUT_MS = 5000;

    private static final String STUDENT_CODE = "B23DCCN947";
    private static final String QUESTION_CODE = "pC5JxSFU";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT)) {
            socket.setSoTimeout(TIMEOUT_MS);

            // Giữ GZIP stream mở để gửi nhiều dòng trên cùng kết nối.
            GZIPOutputStream gzipOutput =
                    new GZIPOutputStream(socket.getOutputStream(), true);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            gzipOutput,
                            StandardCharsets.UTF_8
                    )
            );

            // a. Gửi studentCode;qCode thành một dòng GZIP
            String request = STUDENT_CODE + ";" + QUESTION_CODE;
            writer.write(request);
            writer.write('\n');
            writer.flush();

            System.out.println("Da gui: " + request);

            // b. Giải nén một dòng từ GZIP stream đang mở
            String received = readGzipLine(socket.getInputStream());
            System.out.println("Server gui: " + received);

            // c. Sắp xếp ký tự tăng dần theo mã ASCII
            char[] characters = received.toCharArray();
            Arrays.sort(characters);
            String result = new String(characters);

            // Gửi dòng kết quả qua cùng GZIP stream
            writer.write(result);
            writer.write('\n');
            writer.flush();

            System.out.println("Da gui ket qua: " + result);

        } catch (Exception e) {
            System.out.println("Loi giao tiep: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String readGzipLine(InputStream input) throws Exception {
        // Đọc header GZIP và bỏ các trường tùy chọn nếu có.
        int id1 = readByte(input);
        int id2 = readByte(input);
        int compressionMethod = readByte(input);
        int flags = readByte(input);

        if (id1 != 0x1F || id2 != 0x8B || compressionMethod != 8) {
            throw new java.io.IOException("Phan hoi khong co header GZIP hop le.");
        }

        // Bỏ MTIME, XFL và OS: 6 byte
        for (int i = 0; i < 6; i++) {
            readByte(input);
        }

        // FEXTRA
        if ((flags & 0x04) != 0) {
            int extraLength = readByte(input) | (readByte(input) << 8);
            for (int i = 0; i < extraLength; i++) {
                readByte(input);
            }
        }

        // FNAME
        if ((flags & 0x08) != 0) {
            skipZeroTerminated(input);
        }

        // FCOMMENT
        if ((flags & 0x10) != 0) {
            skipZeroTerminated(input);
        }

        // FHCRC
        if ((flags & 0x02) != 0) {
            readByte(input);
            readByte(input);
        }

        // Giải nén DEFLATE trực tiếp đến newline.
        // Không chờ trailer GZIP vì stream vẫn mở để giao tiếp tiếp.
        Inflater inflater = new Inflater(true);
        ByteArrayOutputStream lineBytes = new ByteArrayOutputStream();
        byte[] compressedByte = new byte[1];
        byte[] outputBuffer = new byte[256];

        try {
            while (true) {
                int count = inflater.inflate(outputBuffer);

                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        int value = outputBuffer[i] & 0xFF;

                        if (value == '\n') {
                            return lineBytes.toString(StandardCharsets.UTF_8)
                                    .stripTrailing();
                        }

                        if (value != '\r') {
                            lineBytes.write(value);
                        }
                    }
                }

                if (inflater.needsDictionary()) {
                    throw new DataFormatException(
                            "GZIP stream can dictionary."
                    );
                }

                if (inflater.needsInput()) {
                    int value = input.read();

                    if (value == -1) {
                        throw new java.io.IOException(
                                "Server dong ket noi truoc khi gui het dong."
                        );
                    }

                    compressedByte[0] = (byte) value;
                    inflater.setInput(compressedByte);
                }
            }
        } finally {
            inflater.end();
        }
    }

    private static int readByte(InputStream input) throws Exception {
        int value = input.read();

        if (value == -1) {
            throw new java.io.IOException("Server dong ket noi som.");
        }

        return value;
    }

    private static void skipZeroTerminated(InputStream input) throws Exception {
        while (readByte(input) != 0) {
            // Bỏ qua đến byte kết thúc trường.
        }
    }
}

//client11

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client11 {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2211;
    private static final String STUDENT_CODE = "B23DCCN947";
    private static final String QUESTION_CODE = "dLd6UHVC";
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng SocketChannel và ByteBuffer để trao đổi thông tin theo giao thức frame: 4 byte độ dài (int32) + payload (UTF-8).

// Lưu ý: server & client đều phải đọc đủ dữ liệu bằng vòng lặp (readFully) do server luôn chia nhỏ dữ liệu khi gửi. Trình tự trao đổi như sau:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;fkdRJYuX

// b. Nhận dữ liệu từ server gồm đúng 3 frame liên tiếp. Payload của mỗi frame là một phần của cùng một HTTP request, client phải nối 3 payload theo đúng thứ tự để thu được chuỗi HTTP request hoàn chỉnh (các dòng phân tách bởi \r\n và kết thúc bằng \r\n\r\n).

// c. Từ chuỗi HTTP request hoàn chỉnh, trích xuất và gửi lại lên server theo định dạng METHOD;PATH;HOST trong đó PATH luôn bao gồm query-string.

// d. Đóng kết nối và kết thúc chương trình.
    public static void main(String[] args) {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.configureBlocking(true);
            channel.socket().setSoTimeout(5000);
            channel.connect(new InetSocketAddress(HOST, PORT));

            // a. Gửi studentCode;qCode dưới dạng một frame
            String login = STUDENT_CODE + ";" + QUESTION_CODE;
            writeFrame(channel, login);

            System.out.println("Da gui: " + login);

            // b. Đọc đúng 3 frame và ghép payload theo thứ tự nhận
            StringBuilder httpRequest = new StringBuilder();

            for (int i = 0; i < 3; i++) {
                String payload = readFrame(channel);
                httpRequest.append(payload);
            }

            System.out.println("HTTP request nhan duoc:");
            System.out.println(httpRequest);

            // c. Trích METHOD, PATH và HOST
            String[] requestInfo = extractRequestInfo(httpRequest.toString());

            String result = requestInfo[0] + ";"
                    + requestInfo[1] + ";"
                    + requestInfo[2];

            writeFrame(channel, result);
            System.out.println("Da gui: " + result);

        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeFrame(SocketChannel channel, String payload)
            throws IOException {
        byte[] data = payload.getBytes(StandardCharsets.UTF_8);

        ByteBuffer frame = ByteBuffer.allocate(Integer.BYTES + data.length);
        frame.putInt(data.length);
        frame.put(data);
        frame.flip();

        while (frame.hasRemaining()) {
            channel.write(frame);
        }
    }

    private static String readFrame(SocketChannel channel) throws IOException {
        // Đọc đủ 4 byte độ dài, kể cả khi server chia nhỏ dữ liệu.
        ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
        readFully(channel, lengthBuffer);
        lengthBuffer.flip();

        int length = lengthBuffer.getInt();

        if (length < 0 || length > 1_000_000) {
            throw new IOException("Do dai frame khong hop le: " + length);
        }

        // Đọc đủ payload theo độ dài vừa nhận.
        ByteBuffer payloadBuffer = ByteBuffer.allocate(length);
        readFully(channel, payloadBuffer);

        return new String(payloadBuffer.array(), StandardCharsets.UTF_8);
    }

    private static void readFully(SocketChannel channel, ByteBuffer buffer)
            throws IOException {
        while (buffer.hasRemaining()) {
            int count = channel.read(buffer);

            if (count == -1) {
                throw new EOFException("Server dong ket noi khi frame chua day du.");
            }
        }
    }

    private static String[] extractRequestInfo(String request)
            throws IOException {
        String[] lines = request.split("\\r?\\n");

        if (lines.length == 0 || lines[0].isBlank()) {
            throw new IOException("HTTP request khong co request line.");
        }

        // Request line có dạng: METHOD PATH HTTP/VERSION
        String[] requestLine = lines[0].trim().split("\\s+");

        if (requestLine.length < 2) {
            throw new IOException("Request line khong dung dinh dang.");
        }

        String method = requestLine[0];
        String path = requestLine[1]; // Giữ nguyên query string nếu có
        String hostValue = null;

        for (String line : lines) {
            int colon = line.indexOf(':');

            if (colon > 0
                    && line.substring(0, colon).trim().equalsIgnoreCase("Host")) {
                hostValue = line.substring(colon + 1).trim();
                break;
            }
        }

        if (hostValue == null || hostValue.isEmpty()) {
            throw new IOException("Khong tim thay header Host.");
        }

        return new String[] { method, path, hostValue };
    }
}

//client12

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2211 (thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng một chương trình client tương tác tới server ở trên sử dụng SocketChannel và ByteBuffer để trao đổi thông tin theo giao thức frame: 4 byte độ dài (int32) + payload (UTF-8).

// Lưu ý: server & client đều phải đọc đủ dữ liệu bằng vòng lặp (readFully) do server luôn chia nhỏ dữ liệu khi gửi. Trình tự trao đổi như sau:

// a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

// Ví dụ: B16DCCN999;ucpQ9zAh

// b. Nhận dữ liệu từ server gồm đúng 2 frame liên tiếp. Payload của mỗi frame là một phần của cùng một chuỗi JSON đơn giản trên một dòng (không xuống dòng). Client phải nối 2 payload theo đúng thứ tự để thu được chuỗi JSON hoàn chỉnh.

// c. Trích xuất các trường event, user, ok và gửi lại lên server theo định dạng event=<event>;user=<user>;ok=<0|1> (true=1, false=0).

// d. Đóng kết nối và kết thúc chương trình.
public class Client12 {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2211;

    private static final String STUDENT_CODE = "B23DCCN947";
    private static final String QUESTION_CODE = "fGaXHzP2";

    public static void main(String[] args) {
        try (SocketChannel channel = SocketChannel.open()) {
            channel.configureBlocking(true);
            channel.connect(new InetSocketAddress(HOST, PORT));

            // a. Gửi studentCode;qCode dưới dạng một frame
            String login = STUDENT_CODE + ";" + QUESTION_CODE;
            writeFrame(channel, login);

            System.out.println("Da gui: " + login);

            // b. Đọc đúng hai frame, ghép payload theo thứ tự
            StringBuilder jsonBuilder = new StringBuilder();

            for (int i = 0; i < 2; i++) {
                jsonBuilder.append(readFrame(channel));
            }

            String json = jsonBuilder.toString();
            System.out.println("JSON nhan duoc: " + json);

            // c. Trích các trường cần thiết
            String event = getJsonString(json, "event");
            String user = getJsonString(json, "user");
            boolean ok = getJsonBoolean(json, "ok");

            String result = "event=" + event
                    + ";user=" + user
                    + ";ok=" + (ok ? "1" : "0");

            writeFrame(channel, result);
            System.out.println("Da gui: " + result);

        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void writeFrame(SocketChannel channel, String payload)
            throws IOException {
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);

        ByteBuffer frame = ByteBuffer.allocate(Integer.BYTES + bytes.length);
        frame.putInt(bytes.length);
        frame.put(bytes);
        frame.flip();

        while (frame.hasRemaining()) {
            channel.write(frame);
        }
    }

    private static String readFrame(SocketChannel channel) throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
        readFully(channel, lengthBuffer);
        lengthBuffer.flip();

        int length = lengthBuffer.getInt();

        if (length < 0 || length > 1_000_000) {
            throw new IOException("Do dai frame khong hop le: " + length);
        }

        ByteBuffer payloadBuffer = ByteBuffer.allocate(length);
        readFully(channel, payloadBuffer);

        return new String(payloadBuffer.array(), StandardCharsets.UTF_8);
    }

    private static void readFully(SocketChannel channel, ByteBuffer buffer)
            throws IOException {
        while (buffer.hasRemaining()) {
            int count = channel.read(buffer);

            if (count == -1) {
                throw new EOFException(
                        "Server dong ket noi khi frame chua du."
                );
            }
        }
    }

    private static String getJsonString(String json, String key)
            throws IOException {
        String pattern = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";

        Matcher matcher = Pattern.compile(pattern).matcher(json);

        if (!matcher.find()) {
            throw new IOException("JSON khong co truong chuoi: " + key);
        }

        return unescapeJsonString(matcher.group(1));
    }

    private static boolean getJsonBoolean(String json, String key)
            throws IOException {
        String pattern = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*(true|false)";

        Matcher matcher = Pattern.compile(pattern).matcher(json);

        if (!matcher.find()) {
            throw new IOException("JSON khong co truong boolean: " + key);
        }

        return Boolean.parseBoolean(matcher.group(1));
    }

    private static String unescapeJsonString(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\/", "/")
                .replace("\\b", "\b")
                .replace("\\f", "\f")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
}