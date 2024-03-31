import org.example.models.Customer;
import org.example.repositories.BaseRepository;
import org.example.repositories.CustomerRepository;
import org.example.utils.Utilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class CustomerRepositoryTests {

        Utilities u = new Utilities();

        private Connection mockConnection;
        private Statement mockStatement;
        private PreparedStatement mockPreparedStatement;
        private ResultSet mockResultSet;
        private ResultSetMetaData mockResultSetMetaData;

        private CustomerRepository customerRepository;


        @BeforeEach
        public void setup() throws SQLException {

            mockConnection = mock(Connection.class);
            mockStatement = mock(Statement.class);
            mockPreparedStatement = mock(PreparedStatement.class);
            mockResultSet = mock(ResultSet.class);
            mockResultSetMetaData = mock(ResultSetMetaData.class);

            var mockColumnNames = new ArrayList<String>();
            mockColumnNames.add("customer_id");
            mockColumnNames.add("customer_no");
            mockColumnNames.add("customer_name");
            mockColumnNames.add("email");
            mockColumnNames.add("phone");
            mockColumnNames.add("address");

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
            when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
            when(mockResultSetMetaData.getColumnCount()).thenReturn(mockColumnNames.size());
            for (int i = 1; i <= mockColumnNames.size(); i++) {
                when(mockResultSetMetaData.getColumnName(i)).thenReturn(u.snakeToCamelCase(mockColumnNames.get(i - 1)));
            }

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getObject(1)).thenReturn(1);
            when(mockResultSet.getObject(2)).thenReturn("1110000001");
            when(mockResultSet.getObject(3)).thenReturn("Test Testsson");
            when(mockResultSet.getObject(4)).thenReturn("test@customer.com");
            when(mockResultSet.getObject(5)).thenReturn("0712345678");
            when(mockResultSet.getObject(6)).thenReturn("TestStreet 101");

//            when(mockResultSet.getObject("customer_id")).thenReturn(1);
//            when(mockResultSet.getObject("customer_no")).thenReturn("1110000001");
//            when(mockResultSet.getObject("customer_name")).thenReturn("Test Testsson");
//            when(mockResultSet.getObject("phone")).thenReturn("0712345678");
//            when(mockResultSet.getObject("email")).thenReturn("test@customer.com");
//            when(mockResultSet.getObject("address")).thenReturn("TestStreet 101");

            customerRepository = new CustomerRepository();
            customerRepository.setConnection(mockConnection);
        }

//        @Test
//        public void addTest() throws SQLException {
//
//            Customer customer = new Customer("Test Testsson", "test@customer.com", "0712345678", "TestStreet 101");
//            customerRepository.add(customer);
//
//            verify(mockConnection).prepareStatement(anyString());
//            verify(mockPreparedStatement).setObject(1, anyString());
//            verify(mockPreparedStatement).setObject(2, customer.getCustomerName());
//            verify(mockPreparedStatement).setObject(3, customer.getEmail());
//            verify(mockPreparedStatement).setObject(4, customer.getPhone());
//            verify(mockPreparedStatement).setObject(5, customer.getAddress());
//            verify(mockPreparedStatement).executeUpdate(anyString());
//        }


        @Test
        public void getAllTest() throws SQLException {
            List<Customer> customers = customerRepository.getAll();

            verify(mockConnection, atLeast(1)).createStatement();
            verify(mockStatement).executeQuery(anyString());
            verify(mockResultSet, atLeastOnce()).next();

            assert customers.size() == 1;

            var customer = customers.get(0);

            assert customer.getCustomerId() == 1;
            assert customer.getCustomerNo().equals("1110000001");
            assert customer.getCustomerName().equals("Test Testsson");
            assert customer.getEmail().equals("test@customer.com");
            assert customer.getPhone().equals("0712345678");
            assert customer.getAddress().equals("TestStreet 101");
        }

}
