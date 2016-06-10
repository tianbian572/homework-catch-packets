package zhuabao0527;
//ѡ����������Ϣ
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
	/*����Լ���;*/
	private JPanel contentPane;
	private JTextField cardDes;
	private JComboBox<String> comboBox;//����ѡ��
	private JTextField MacAddtext;//Mac��ַ��Ϣ��
	private JTextField IPAddtext;//IP��ַ��Ϣ��
	private JCheckBox ifPromics ;//����ģʽѡ��
	private JSpinner sizeSpinner;//ѡ����������ݰ���С
	private JTextField filterText;//�������ı���
	/*�����ڲ�����*/
	private Vector<String> cards;
	private Map<String, jpcap.NetworkInterface> mpCards;
	
	/*���ڸ���һ�����ڴ�ֵ*/
	private static NetworkInterface selectedCard=null;//��ѡ������
	private static boolean HunZa=false;//����ģʽ,Ĭ��Ϊ��
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
	//���ر�ѡ�������
	public static NetworkInterface getNetworkInterface()
	{
		if(selectedCard!=null)
			return selectedCard;//���ر�ѡ�������
		else
			//JOptionPane
			return null;
	}
	//�����Ƿ����
	public static boolean getHunZa()
	{
		return HunZa;
	}
	//���ع�����
	public static String getFilter()
	{
		return filter;
	}
	//�������ݱ���С
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
		setTitle("ѡ������");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 393);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();//��ȡȫ������
		
		cards =new Vector<String>();//���ڴ��������������
		mpCards =  new HashMap<String, NetworkInterface>();//key-������,val-����
		
		
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
					IPAddtext.setText("IP��ַ��"+a.address+"\t"+"�������룺"+a.subnet+"\t"+"�㲥��ַ��"+a.broadcast);
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
		
		JButton button = new JButton("ȷ  ��");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				selectedCard=mpCards.get(comboBox.getSelectedItem());
				maxPacketSize=Integer.parseInt(sizeSpinner.getValue().toString());//�趨���ݰ���С
				filter=filterText.getText();
				//System.out.println("����ʹ�ã�"+filter);
				
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
		
		ifPromics = new JCheckBox("����ģʽ");
		ifPromics.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) //ѡ���Ƿ�Ϊ����ģʽ
			{
				if(ifPromics.isSelected())
					HunZa=true;
				else HunZa=false;
			}
		});
		
		sizeSpinner = new JSpinner();
		sizeSpinner.setModel(new SpinnerNumberModel(65535, 1, 65535, 1));
		
		JLabel label_2 = new JLabel("���ݱ����������");
		
		JButton buttonCancle = new JButton("��    ��");
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
