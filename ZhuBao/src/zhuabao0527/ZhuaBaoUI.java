package zhuabao0527;
//选择网卡等信息
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import jpcap.*;
import jpcap.packet.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ZhuaBaoUI extends JFrame
{
	/*组件以及用途*/
	private JPanel contentPane;
	private JTextField cardDes;
	private JComboBox<String> comboBox;//网卡选择
	private JTextField MacAddtext;//Mac地址信息窗
	private JTextField IPAddtext;//IP地址信息窗
	private JCheckBox ifPromics ;//混杂模式选择
	private JSpinner sizeSpinner;//选择做大的数据包大小
	private JTextField filterText;//过滤器文本框
	/*本类内部变量*/
	private Vector<String> cards;
	private Map<String, jpcap.NetworkInterface> mpCards;
	
	/*用于给下一个窗口传值*/
	private static NetworkInterface selectedCard=null;//被选择网卡
	private static boolean HunZa=false;//混杂模式,默认为否
	private static int maxPacketSize=65535;
	private static String filter=null;
	

	
	public static void main(String[] args)
	{
	
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ZhuaBaoUI frame = new ZhuaBaoUI();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	//返回被选择的网卡
	public static NetworkInterface getNetworkInterface()
	{
		if(selectedCard!=null)
			return selectedCard;//返回被选择的网卡
		else
			//JOptionPane
			return null;
	}
	//返回是否混杂
	public static boolean getHunZa()
	{
		return HunZa;
	}
	//返回过滤器
	public static String getFilter()
	{
		return filter;
	}
	//返回数据报大小
	public static int getMaxPcketSize()
	{
		return maxPacketSize;
	}
	

	public ZhuaBaoUI() throws UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		setResizable(false);
		//this.setVisible(true);
		setEnabled(true);
		setTitle("选择网卡");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();//获取全部网卡
		
		cards =new Vector<String>();//用于存放所有网卡名称
		mpCards =  new HashMap<String, NetworkInterface>();//key-网卡名,val-网卡
		
		
		for(int i=0;i<devices.length;i++)
		{
			mpCards.put(devices[i].name, devices[i]);
			cards.add(devices[i].name);
		}
		
		comboBox = new JComboBox<String>(cards);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				NetworkInterface item=mpCards.get(comboBox.getSelectedItem().toString());
				cardDes.setText(item.description+"\t"+item.datalink_name+"\t"+item.datalink_description);
				
				for(NetworkInterfaceAddress a:item.addresses)
				{
					IPAddtext.setText("IP地址："+a.address+"\t"+"子网掩码："+a.subnet+"\t"+"广播地址："+a.broadcast);
					//IPAddtext.
				}
				String macString="";
				for (byte b : item.mac_address)
					macString+=Integer.toHexString(b&0xff) + "  ";
				MacAddtext.setText(macString);
			}
		});
		
		
		cardDes = new JTextField();
		cardDes.setEditable(false);
		cardDes.setColumns(10);
		
		JLabel label = new JLabel("\u7F51\u5361\u540D\u79F0");
		
		JLabel label_1 = new JLabel("\u7F51\u5361\u4FE1\u606F");
		
		JLabel lblWangka = new JLabel("\u7269\u7406\u5730\u5740");
		
		MacAddtext = new JTextField();
		MacAddtext.setEditable(false);
		MacAddtext.setColumns(10);
		
		JLabel lebalfilter = new JLabel("IP \u5730 \u5740");
		
		IPAddtext = new JTextField();
		IPAddtext.setEditable(false);
		IPAddtext.setColumns(10);
		
		JButton button = new JButton("确  定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				selectedCard=mpCards.get(comboBox.getSelectedItem());
				maxPacketSize=Integer.parseInt(sizeSpinner.getValue().toString());//设定数据包大小
				filter=filterText.getText();
				//System.out.println("调试使用："+filter);
				
				try
				{
					TableFrame zhuaFrame = new TableFrame();
					zhuaFrame.zhu();
				} catch (Exception e2)
				{
					System.exit(1);
				}
			}
		});
		
		ifPromics = new JCheckBox("混杂模式");
		ifPromics.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) //选择是否为混杂模式
			{
				if(ifPromics.isSelected())
					HunZa=true;
				else HunZa=false;
			}
		});
		
		sizeSpinner = new JSpinner();
		sizeSpinner.setModel(new SpinnerNumberModel(65535, 1, 65535, 1));
		
		JLabel label_2 = new JLabel("数据报最大数据量");
		
		JButton buttonCancle = new JButton("关    闭");
		buttonCancle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				System.exit(0);
			}
		});
		
		JLabel lblNewLabel = new JLabel("\u8BBE\u7F6E\u8FC7\u6EE4\u5668");
		
		filterText = new JTextField();
		filterText.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(label_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblWangka, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lebalfilter, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 10, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(IPAddtext, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
						.addComponent(MacAddtext, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
						.addComponent(cardDes, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
						.addComponent(comboBox, Alignment.TRAILING, 0, 551, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(ifPromics)
					.addGap(44)
					.addComponent(label_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sizeSpinner, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(40)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(filterText, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(429, Short.MAX_VALUE)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(buttonCancle, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(12))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(label, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(label_1, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
						.addComponent(cardDes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWangka, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
						.addComponent(MacAddtext, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lebalfilter, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
						.addComponent(IPAddtext, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(ifPromics)
						.addComponent(label_2)
						.addComponent(sizeSpinner, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel)
						.addComponent(filterText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(82)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonCancle, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
