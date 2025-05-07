package org.example.medlink.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;


import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    @GetMapping("/stream")
    public void streamPrediction(@RequestParam String disease, HttpServletResponse response) {

//        System.out.println("Received disease param: " + disease);

        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        try {
            // 启动 Python 预测脚本
            ProcessBuilder pb = new ProcessBuilder(
                    "D:\\Software\\anaconda3\\envs\\DRAGNN\\python.exe",
                    "-u", "main.py", "--mode", "case", "--specific_name", disease
            );
            pb.directory(new File("src/main/resources/model/DRAGNN"));  // 设置工作目录
            pb.redirectErrorStream(true);  // 合并 stderr 和 stdout
            Process process = pb.start();

            PrintWriter writer = response.getWriter();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write("data: " + line + "\n\n");
                writer.flush();  // 刷新缓冲区，确保前端能收到数据
            }

            process.waitFor();  // 等待子进程结束

            // 拼接输出文件路径
            String resultFileName = disease + "-potential-drug.txt";
            File resultFile = new File("src/main/resources/model/DRAGNN/result/Fdataset", resultFileName);

            if (resultFile.exists()) {
                List<String> drugs = Files.readAllLines(resultFile.toPath());
                for (String drug : drugs) {
                    writer.write("event: result\ndata: " + drug + "\n\n");
                    writer.flush();
                }
            } else {
                writer.write("event: result\ndata: 未找到预测结果文件\n\n");
                writer.flush();
            }

            // 通知前端结束
            writer.write("event: end\ndata: done\n\n");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                PrintWriter writer = response.getWriter();
                writer.write("data: [ERROR] " + e.getMessage() + "\n\n");
                writer.flush();
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
