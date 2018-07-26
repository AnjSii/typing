import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.*;

public class Typing {

	private JFrame frame = new JFrame("Typing");

	private JSplitPane splitPane = new JSplitPane();

	private JButton finishBtn = new JButton("结束");

	private JButton resultsBtn = new JButton("结果");

	private JButton exitBtn = new JButton("退出");

	private JTextArea textArea = new JTextArea(5, 10);

	private JScrollPane scrollPane = new JScrollPane(textArea);

	private Date startTime;

	private Date endTime;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Typing();
			}
		});
	}

	private Typing() {

		splitPaneComponent();
		textAreaAction();
		finishBtnAction();
		resultsBtnAction();
		exitBtnAction();

		frame.setUndecorated(true);//去处边框
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //最大化
		frame.setResizable(false); //不能改变大小
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		frame.add(splitPane);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void splitPaneComponent() {

		Font textFont = new Font("宋体", Font.PLAIN, 20);
		textArea.setFont(textFont);
		textArea.setLineWrap(true);

		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(scrollPane);
		splitPane.setRightComponent(finishBtn);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();// 获取屏幕的边界
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// 获取底部任务栏高度
		int monitorFrameHeight = screenSize.height - screenInsets.top - screenInsets.bottom;

		String osName = System.getProperty("os.name");
		splitPane.setDividerSize(2);
		if (osName.contains("Mac")) {
			splitPane.setSize(screenSize.width, monitorFrameHeight);
			splitPane.setDividerLocation(monitorFrameHeight - 50);
		} else if (osName.contains("Windows")) {
			splitPane.setSize(screenSize.width, screenSize.height);
			splitPane.setDividerLocation(screenSize.height - 50);
		}
	}

	private void textAreaAction() {
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { //敲击键盘，发生在按键按下后，按键放开前
				if (textArea.getText().equals("")) {
					startTime = new Date();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) { // 按下按键

			}

			@Override
			public void keyReleased(KeyEvent e) {  //松开按键

			}
		});
	}

	private void finishBtnAction() {
		finishBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textArea.getText().equals("")) {
					finishBtnChange();
				} else {
					JOptionPane.showMessageDialog(null, "请输入文字", "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private void finishBtnChange() {
		endTime = new Date();

		textArea.setEnabled(false);

		JSplitPane bottomSplitPane = new JSplitPane();
		bottomSplitPane.setLeftComponent(resultsBtn);
		bottomSplitPane.setRightComponent(exitBtn);
		bottomSplitPane.setDividerSize(2);
		bottomSplitPane.setDividerLocation(splitPane.getWidth() / 2);

		splitPane.setRightComponent(bottomSplitPane);
		splitPane.setDividerLocation(splitPane.getHeight() - 50);
		showMsg();
	}

	private void showMsg() {
		long time = endTime.getTime() - startTime.getTime();
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;
		JOptionPane.showMessageDialog(null,
				"用时: " + minutes + " 分 " + seconds + " 秒 \n" + "相似度: " + result(),
				"结果", JOptionPane.INFORMATION_MESSAGE);
	}

	private String result() {
		String userArticle = textArea.getText();
		String contrastArticle = "  I am the victim of an internet scam. It is very hard to write that sentence, but it's necessary in order to warn my fellow clowns, magicians and other entertainers, and to prevent them from being taken for $2,800.00 like I was. Here is what happened to me, and how you can avoid it.\n" +
				"  I run the clown-ministry.com web site, and I perform as a clown at both ministry and secular events. Due to the popularity of the clown-ministry.com web site (do a search on Google .com for 'clown' and it's the 7th site listed, for example) I'm easily found by people -- for both good and bad reasons. For instance, last year my wife Ellen and I taught clowning for a week at the Seventh Day Adventists' national Camporee -- we're not Adventists, and don't know many people who are, but we were found by them & hired primarily due to the popularity of the web site. We've been invited to perform in clown at a conference in Canada, performed at company picnics, etc. well out of our neck of the wood, and so on. Please understand that this isn't bragging; it's setting the stage for what happened in the scam.\n" +
				"  We were contacted via e-mail by someone purportedly from Cincinnati, Ohio, who wanted to fly my wife & I to Cincinnati to perform at a birthday party. Since we'd had similar things happen in the past, that didn't set off any alarms in my head. In addition, I knew that there are entertainers such as Jennifer Lopez who will sing \"Happy Birthday\" at your party, for a fee of $50,000.00; I'm no J Lo, and my singing is atrocious, but I charge a lot less as well.\n" +
				"  I did an internet search on that person (I'm not using the name he gave, since there's a criminal investigation as well as FBI investigation going on -- no joke), and found someone by that name in Cincinnati, Ohio, a \"regional celebrity\" so to speak. So, it didn't seem incredulous that he would have the money to hire us as well as pay round trip air fare. It's now obvious that it was merely someone using that person's name to \"hook\" us.\n" +
				"  At that point, my wife & I were still inclined to think of this as a prank, but even so we replied to the email with a quotation for our services, politely and professionally. We were joking with each other that this had to be a prank; after all, why would someone hire us when there are hundreds of clowns in the Cincinnati area? Unless it was someone with money to burn who wanted to buy \"bragging rights,\" it made no sense. So we forgot about it, until the person replied back, hiring us.\n" +
				"  We still considered it a joke, until we received a check in the mail, for $2,800.00. MUCH more that we had quoted our services for! We thought that, maybe, the excess was to pay for round trip air fare and accommodations, but even so that was far too much. We e-mailed the person back, explaining that we had received $2,800.00 via check. His response is what should have tipped us off to the scam.\n" +
				"  He replied that, since he was out of the country on a Business trip, he had his personal assistant send the checks out for the party, and the assistant had made a mistake, combining with our check one for $2,300.00 for the photographer from the United Kingdom that he was flying in to photograph the party. Would we be so kind as to send him those funds via Western Union?\n" +
				"  Like an idiot, I said \"yes.\" The check had been deposited and cleared (I thought), and I didn't want to hold onto money that didn't belong to me. So, I went to the local Western Union office and sent $2,300.00 to an individual in the United Kingdom. This didn't set off any red flags (although it should have) since it seemed like the type of silly mistake I've seen people do before. What happened next, though, set off every red flag in my head, and then some.\n" +
				"  Days later, I received another email from the man supposedly from Cincinnati. According to him, his mother had an advanced form of lung cancer, and needed an immediate operation. He wanted me to send the remaining money to his mother's doctor in Nigeria and he would reschedule the party later. When I heard the word 'Nigeria' I had icicles running up and down my spine. Nigeria is famous, in the internet world, for being Home to the most prolific scam of all time. You've likely received spam email about it. In a nutshell, it involves the widow of some minister or rich person, who is trying to smuggle their immense fortune out of the country before it's seized by her dead husband's adversaries; in exchange for letting them use your bank account, they will give you (various amounts of money, if not enough to make you filthy rich, at least enough to make you dingy\n";
		EditDistanceCalculator editDistanceCalculator = new EditDistanceCalculator();
		float similarity =  editDistanceCalculator.correct(contrastArticle, userArticle);

		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMaximumFractionDigits(5);

		return numberFormat.format(similarity);
	}

	private void resultsBtnAction() {
		resultsBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMsg();
			}
		});
	}

	private void exitBtnAction () {
		exitBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"程序即将退出...");
				System.exit(0);
			}
		});
	}
}
