import java.awt.*;
import java.util.Date;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

class SwingModule {

	JFrame frame = new JFrame("Typing");

	JSplitPane splitPane = new JSplitPane();

	JButton finishBtn = new JButton("结束");

	JButton exitBtn = new JButton("退出");

	JButton restartBtn = new JButton("重新开始");

	protected JTextArea textArea = new JTextArea(5, 10) {
		@Override public void copy(){}
		@Override public void paste(){}
	};

	JToolBar toolBar = new JToolBar();

	JComboBox font = new JComboBox();

	JLabel textFont = new JLabel("字体");

	String current_fontName = "宋体";

	JLabel textFontSize = new JLabel("字体大小");

	JComboBox fontSize = new JComboBox();

	Map sizeMap;

	int current_fontSize = 12;//当前字体大小,默认12号.

	JComboBox fontStyle = new JComboBox();

	JLabel textStyle = new JLabel("字型" );

	int current_fontStyle = Font.PLAIN;

	JScrollPane scrollPane = new JScrollPane(textArea);

	static JFileChooser fileChooser = new JFileChooser();

	StyledDocument styledDoc = new DefaultStyledDocument();

	JTextPane leftTextPane = new JTextPane(styledDoc);

	JScrollPane letfScroll = new JScrollPane(leftTextPane);

	JTextPane rightTextPane = new JTextPane();

	JScrollPane rightScroll = new JScrollPane(rightTextPane);

	JTextArea resutTextArea = new JTextArea();

	JScrollPane resutScrollPane = new JScrollPane(resutTextArea);

	JSplitPane showDiffSplitPane = new JSplitPane();

	JSplitPane showResutSplitPane = new JSplitPane();

	JSplitPane bottomSplitPane = new JSplitPane();

	int delCount;

	long numberOfWords;

	Date startTime = new Date();

	Date endTime;
}
