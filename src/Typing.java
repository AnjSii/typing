import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Typing extends SwingModule {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Typing::new);
	}

	private Typing() {

		fontSet();
		splitPaneComponent();
		textAreaKeyAction();
		textAreaWordAction();
		fontAction();
		fontSizeAction();
		fontStyleAction();
		finishBtnAction();
		exitBtnAction();
		restartBtnAction();

		frame.setUndecorated(true);//去处边框
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //最大化
		frame.setResizable(false); //不能改变大小
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		frame.add(splitPane, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		toolBar.setFloatable(false);
		toolBar.add(textFont);
		toolBar.add(font);
		toolBar.add(textFontSize);
		toolBar.add(fontSize);
		toolBar.add(textStyle);
		toolBar.add(fontStyle);
	}

	private void splitPaneComponent() {

		textArea.setColumns(140);
		textArea.setRows(24);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMargin(new Insets(25, 25, 25, 25));
		textArea.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));

		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(finishBtn);
		splitPane.setBorder(null);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的边界
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// 获取底部任务栏高度
		int monitorFrameHeight = screenSize.height - screenInsets.top - screenInsets.bottom;

		String osName = System.getProperty("os.name");
		splitPane.setDividerSize(0);
		if (osName.contains("Mac")) {
			splitPane.setSize(screenSize.width, monitorFrameHeight);
			splitPane.setDividerLocation(monitorFrameHeight - 70);
		} else if (osName.contains("Windows")) {
			splitPane.setSize(screenSize.width, screenSize.height);
			splitPane.setDividerLocation(screenSize.height - 70);
		}
	}

	private void splitPaneAlterComponent(String fileText) {

		endTime = new Date();

		EditDistanceCalculator editDistanceCalculator = new EditDistanceCalculator();
		float similarity = editDistanceCalculator.correct(textArea.getText(), fileText.replaceAll("\\n*$", ""));

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMaximumFractionDigits(5);

		String resut = numberFormat.format(similarity);

		long time = endTime.getTime() - startTime.getTime();
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;

		resutTextArea.setFont(new Font("宋体", Font.PLAIN, 16));
		resutTextArea.setFocusable(false);
		if (minutes > 0) {
			numberOfWords = textArea.getText().length() / minutes;
			resutTextArea.setText("绿色为多打的字符，红色为少打的字符\n\n" + "正确率为：" + resut + "\n\n" + "打字平均速度:"
					+ numberOfWords + "字/分" + "\n\n" + "退格：" + delCount + " 次");
		} else {
			numberOfWords = textArea.getText().length() / seconds;
			resutTextArea.setText("绿色为多打的字符，红色为少打的字符\n\n" + "正确率为：" + resut + "\n\n" + "打字平均速度:"
					+ numberOfWords + "字/秒" + "\n\n" + "退格：" + delCount + " 次");
		}
		resutScrollPane = new JScrollPane(resutTextArea);

		DiffMatchPatch dmp = new DiffMatchPatch(styledDoc);

		dmp.diffString(textArea.getText(), fileText.replaceAll("\\n*$", ""));
		leftTextPane.setFocusable(false);
		leftTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
		letfScroll = new JScrollPane(leftTextPane);
		letfScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));

		rightTextPane.setText(fileText.replaceAll("\\n*$", ""));
		rightTextPane.setFocusable(false);
		rightTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
		rightScroll = new JScrollPane(rightTextPane);
		rightScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));

		showDiffSplitPane.setLeftComponent(letfScroll);
		showDiffSplitPane.setRightComponent(rightScroll);
		showDiffSplitPane.setDividerSize(0);
		showDiffSplitPane.setBorder(null);

		showResutSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		showResutSplitPane.setLeftComponent(showDiffSplitPane);
		showResutSplitPane.setRightComponent(resutScrollPane);
		showResutSplitPane.setDividerSize(0);
		showResutSplitPane.setBorder(null);

		bottomSplitPane.setLeftComponent(restartBtn);
		bottomSplitPane.setRightComponent(exitBtn);
		bottomSplitPane.setDividerSize(0);
		bottomSplitPane.setDividerLocation(splitPane.getWidth() / 2);
		bottomSplitPane.setBorder(null);

		splitPane.setLeftComponent(showResutSplitPane);
		splitPane.setRightComponent(bottomSplitPane);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的边界
		String osName = System.getProperty("os.name");
		if (osName.contains("Mac")) {
			showDiffSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showDiffSplitPane.setDividerLocation(screenSize.width / 2);
			showResutSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showResutSplitPane.setDividerLocation(splitPane.getHeight() - 200);
			splitPane.setDividerLocation(screenSize.height - 100);
		} else if (osName.contains("Windows")) {
			showDiffSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showDiffSplitPane.setDividerLocation(screenSize.width / 2);
			showResutSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showResutSplitPane.setDividerLocation(splitPane.getHeight() - 200);
			splitPane.setDividerLocation(screenSize.height - 100);
		}
		scrollAction();
	}

	private void resutComponent(String fileText) {
		EditDistanceCalculator editDistanceCalculator = new EditDistanceCalculator();
		float similarity = editDistanceCalculator.correct(textArea.getText(), fileText.replaceAll("\\n*$", ""));

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMaximumFractionDigits(5);

		String resut = numberFormat.format(similarity);

		long time = endTime.getTime() - startTime.getTime();
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);

		if (minutes == 0) {
			minutes = 1;
		}

		resutTextArea.setFont(new Font("宋体", Font.PLAIN, 16));
		resutTextArea.setFocusable(false);

		numberOfWords = textArea.getText().length() / minutes;
		resutTextArea.setText("正确率为：" + resut + "\n\n" + "打字平均速度:"
				+ numberOfWords + "字/分" + "\n\n" + "退格：" + delCount + " 次");

		resutScrollPane = new JScrollPane(resutTextArea);

		bottomSplitPane.setLeftComponent(restartBtn);
		bottomSplitPane.setRightComponent(exitBtn);
		bottomSplitPane.setDividerSize(0);
		bottomSplitPane.setDividerLocation(splitPane.getWidth() / 2);
		bottomSplitPane.setBorder(null);

		splitPane.setLeftComponent(resutScrollPane);
		splitPane.setRightComponent(bottomSplitPane);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的边界
		String osName = System.getProperty("os.name");
		if (osName.contains("Mac")) {
			showDiffSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showDiffSplitPane.setDividerLocation(screenSize.width / 2);
			showResutSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showResutSplitPane.setDividerLocation(splitPane.getHeight() - 200);
			splitPane.setDividerLocation(screenSize.height - 100);
		} else if (osName.contains("Windows")) {
			showDiffSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showDiffSplitPane.setDividerLocation(screenSize.width / 2);
			showResutSplitPane.setSize(screenSize.width, splitPane.getHeight());
			showResutSplitPane.setDividerLocation(splitPane.getHeight() - 200);
			splitPane.setDividerLocation(screenSize.height - 100);
		}
	}

	private void textAreaKeyAction() {
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { //敲击键盘，发生在按键按下后，按键放开前

			}

			@Override
			public void keyPressed(KeyEvent e) { // 按下按键
				if (e.getKeyCode() == 8) {
					delCount++;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {  //松开按键
			}
		});
	}

	private void textAreaWordAction() {
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (textArea.getText().length() == 1) {
					startTime = new Date();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {

			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
	}

	private void finishBtnAction() {
		finishBtn.addActionListener(e -> {
			if (!textArea.getText().equals("")) {
				endTime = new Date();
				loadFile();
			} else {
				JOptionPane.showMessageDialog(null, "请输入文字", "提示", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void scrollAction() {
		JScrollBar leftbar = letfScroll.getVerticalScrollBar();
		leftbar.addAdjustmentListener(e -> rightScroll.getVerticalScrollBar().setValue(e.getValue()));

		JScrollBar rightbar = rightScroll.getVerticalScrollBar();
		rightbar.addAdjustmentListener(e -> letfScroll.getVerticalScrollBar().setValue(e.getValue()));
	}

	private void exitBtnAction() {
		exitBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "程序即将退出...");
				System.exit(0);
			}
		});
	}

	private void restartBtnAction() {
		restartBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.removeAll();
				frame.pack();
				new Typing();
			}
		});
	}

	private void loadFile() {
		String fileText = null;

		fileChooser.setDialogTitle("请选择文章");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
		fileChooser.setFileFilter(filter);

		int returnVal = fileChooser.showOpenDialog(frame);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			if (file != null) {
				String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
				if (!suffix.equals("txt")) {
					JOptionPane.showMessageDialog(null,
							"请选择后缀为.txt的文件", "文件错误", JOptionPane.ERROR_MESSAGE);
				}
				fileText = file.getAbsolutePath();
			} else {
				JOptionPane.showMessageDialog(null, "选择文件失败，请重新选择", "操作失误", JOptionPane.ERROR_MESSAGE);
			}
			try {
				/*splitPaneAlterComponent(readFile(fileText));*/
				resutComponent(readFile(fileText));
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "选择文件失败，请重新选择", "操作失误", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	private String readFile(String file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));

		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}

		return sb.toString();
	}

	private void fontSet() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

		font.addItem("宋体");
		for (String fontName : fontNames) {
			font.addItem(fontName);
		}

		font.setMaximumSize(font.getPreferredSize());

		//字号.
		String[] fontSizeNames = new String[]{
				"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72", "初号", "小初",
				"一号", "小一", "二号", "小二", "三号", "小三", "四号", "小四", "五号", "小五", "六号", "小六", "七号", "八号"
		};
		int sizeVal[] = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72, 42, 36, 26, 24, 22, 18, 16, 15, 14, 12, 11, 9, 8, 7, 6, 5};
		sizeMap = new HashMap();
		for (int i = 0; i < fontSizeNames.length; ++i) {
			sizeMap.put(fontSizeNames[i], sizeVal[i]);
		}

		for (String fontSizeName : fontSizeNames) {
			fontSize.addItem(fontSizeName);
			if (fontSizeName.equals("12")) {
				fontSize.setSelectedItem(fontSizeName);
			}
		}

		String[] fontStyleNames = new String[]{
				"常规", "粗休", "斜休", "粗斜休"
		};

		for (String fontStyleName : fontStyleNames) {
			fontStyle.addItem(fontStyleName);
		}

		fontSize.setMaximumSize(fontSize.getPreferredSize());
	}

	private void fontAction() {
		font.addActionListener(e -> {
			current_fontName = (String) font.getSelectedItem();
			textArea.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			leftTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			rightTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			letfScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
			rightScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
		});
	}

	private void fontSizeAction() {
		fontSize.addActionListener(e -> {
			current_fontSize = (Integer) sizeMap.get((String) fontSize.getSelectedItem());
			textArea.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			leftTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			rightTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			letfScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
			rightScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
		});
	}

	private void fontStyleAction() {
		fontStyle.addActionListener(e -> {
			String fontStyleValue = (String) fontStyle.getSelectedItem();
			if (fontStyleValue != null) {
				switch (fontStyleValue) {
					case "常规":
						current_fontStyle = Font.PLAIN;
						break;
					case "斜休":
						current_fontStyle = Font.ITALIC;
						break;
					case "粗休":
						current_fontStyle = Font.BOLD;
						break;
					case "粗斜休":
						current_fontStyle = Font.BOLD | Font.ITALIC;
						break;
				}
			}
			textArea.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			rightTextPane.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			letfScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
			rightScroll.setRowHeaderView(new LineNumberHeaderView(current_fontName, current_fontStyle, current_fontSize));
		});
	}
}
