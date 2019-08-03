package com.mejesticpay.cifservice;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mejesticpay.cifservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CIFLoadService
{
    Gson gson = new Gson();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/customers")
    public ResponseEntity<String> uploadFile(@RequestPart() MultipartFile file)
    {
        try
        {
            InputStream in = file.getInputStream();

            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<Customer> messages = new ArrayList<Customer>();
            reader.beginArray();
            while (reader.hasNext())
            {
                Customer customer = gson.fromJson(reader, Customer.class);
                customerRepository.save(customer);
            }
            reader.endArray();
            reader.close();

        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return new ResponseEntity<String>("Failed to process the file  ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("Successfully processed the file", HttpStatus.OK);
    }

}
