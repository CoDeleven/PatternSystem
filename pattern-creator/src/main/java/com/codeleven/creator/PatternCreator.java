package com.codeleven.creator;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.codeleven.common.constants.ShoesSize;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;

public class PatternCreator extends JFrame{
    private JTextField 助记符输入框;
    private JRadioButton 上亿系统RadioButton;
    private JRadioButton 大豪系统RadioButton;
    private JComboBox 鞋子尺码列表框;
    private JButton 上传封面Button;
    private JButton 上传数据Button;
    private JLabel 数据上传结果;
    private JButton 提交数据Button;
    private JPanel myRootPanel;
    private ButtonGroup patternVendorGroup;

//    private ShoesPatternO vo = new ShoesPatternVO();
//
//    public PatternCreator() {
//        上传封面Button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser chooser = new JFileChooser();
//                File currentDir = new File("C:\\Users\\Administrator\\Desktop");
//                chooser.setCurrentDirectory(currentDir);
//                chooser.setDialogTitle("请选择图片文件...");
//                chooser.setFileFilter(new FileNameExtensionFilter("常用图片格式", "jpg", "jpeg", "gif", "png"));
//                chooser.showOpenDialog(PatternCreator.this);
//                File file = chooser.getSelectedFile();
//                try {
//                    String coverPath = MinioUtil.uploadPatternCover(file.getName(), new FileInputStream(file));
//                    vo.setCoverPath(coverPath);
//                } catch (FileNotFoundException fileNotFoundException) {
//                    fileNotFoundException.printStackTrace();
//                    return;
//                }
//
//                JOptionPane.showMessageDialog(
//                        PatternCreator.this,
//                        "图片上传成功",
//                        "ok",
//                        JOptionPane.INFORMATION_MESSAGE
//                );
//            }
//        });
//
//        this.setContentPane(this.myRootPanel);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.pack();
//        this.setVisible(true);
//        上传数据Button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser chooser = new JFileChooser();
//                File currentDir = new File("C:\\Users\\Administrator\\Desktop");
//                chooser.setCurrentDirectory(currentDir);
//                chooser.setDialogTitle("请选择花样数据...");
//                chooser.setFileFilter(new FileNameExtensionFilter("花样数据格式", "NPT", "NSP"));
//                chooser.showOpenDialog(PatternCreator.this);
//                File file = chooser.getSelectedFile();
//
//                try {
//                    String patternPath = MinioUtil.uploadPatternData(file.getName(), new FileInputStream(file));
//                    vo.setPatternDataPath(patternPath);
//                } catch (FileNotFoundException fileNotFoundException) {
//                    fileNotFoundException.printStackTrace();
//                    return;
//                }
//
//                JOptionPane.showMessageDialog(
//                        PatternCreator.this,
//                        "数据上传成功",
//                        "ok",
//                        JOptionPane.INFORMATION_MESSAGE
//                );
//            }
//        });
//        提交数据Button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String name = 助记符输入框.getText();
//                Enumeration<AbstractButton> elements = patternVendorGroup.getElements();
//                while(elements.hasMoreElements()){
//                    AbstractButton btn = elements.nextElement();
//                    String selectText = btn.getText();
//                    if("上亿系统".equals(selectText)){
//                        vo.setVendor(PatternSystemVendor.SYSTEM_TOP.getValue());
//                    } else if("大豪系统".equals(selectText)){
//                        vo.setVendor(PatternSystemVendor.DAHAO.getValue());
//                    }
//                }
//                String selectedItem = (String)鞋子尺码列表框.getSelectedItem();
//                ShoesSize shoesSize = ShoesSize.getShoesSize(Integer.parseInt(selectedItem + "0"));
//                vo.setSize(shoesSize.getSize());
//                vo.setName(name);
//
//                assert StrUtil.isNotBlank(selectedItem);
//                assert StrUtil.isNotBlank(vo.getName());
//                assert StrUtil.isNotEmpty(vo.getCoverPath());
//                assert StrUtil.isNotEmpty(vo.getPatternDataPath());
//                assert vo.getVendor() != 0;
//
//                System.out.println(JSONUtil.toJsonStr(vo));
//                HttpResponse response = HttpUtil.createPost("http://woekeven.natapp1.cc/pattern/create").body(JSONUtil.toJsonStr(vo)).execute();
//                String body = response.body();
//                int status = response.getStatus();
//                if(status != 200){
//                    System.out.println(body);
//                }
//                vo = new ShoesPatternVO();
//                clearText();
//            }
//        });
//    }

    private void clearText(){
        助记符输入框.setText("");
    }

    public static void main(String[] args) {
        new PatternCreator();
    }
}
