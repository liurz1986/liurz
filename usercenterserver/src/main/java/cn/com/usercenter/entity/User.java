package cn.com.usercenter.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Index;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;


//name,password为联合索引（联合索引是指联合在一起的字段是唯一），主键名设置为user,unique为true表示唯一;uuid 为唯一索引
@Entity
@Table(name = "t_user_1",indexes={@Index(name="user",columnList="name,password",unique=true),@Index(name="uuid",columnList="uuid",unique=true)})
public class User {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO) 分表分库时需要注释掉
	private int id;

	// 唯一标识，不能为空 nullable定义是不是可以为空，默认时true可以是为空，length定义属性最大长度
	@Column(name = "uuid", nullable = false, length = 40)
	private String uuid;
	
	@Column(name = "name", nullable = false, length = 30)
	private String name;

	@Column(name = "password", nullable = false, length = 50)
	private String password;
	
	@Column(name = "email", nullable = false, length = 200)
	private String email;
	
	@Column(name = "status", nullable = false, length = 20)
	private String status;//用户状态 可用或不可用（yes或no）
	/***
	 * 第一次查入数据库表的时间(自动创建)，后期修改记录了，该值不会改变（@CreationTimestamp）
	 * @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 时间戳生成格式	
	 * @CreationTimestamp // 产生时间戳 自动生成
	 * @Temporal(TemporalType.TIMESTAMP)
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 时间戳生成格式
	@CreationTimestamp // 产生时间戳 自动生成
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;//创建时间  （因为需要外部录入数据，不能自动生成日期）
   /***
    * 每次录入数据的时间(自动创建)，后期修改记录了，该值会随之改变（@UpdateTimestamp）
    * @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 时间戳生成格
    * @UpdateTimestamp // 更新时间戳 自动生成
    * @Temporal(TemporalType.TIMESTAMP)
    */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 时间戳生成格
	@UpdateTimestamp // 更新时间戳 自动生成
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;//更新时间   （因为需要外部录入数据，不能自动生成日期）

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 时间戳生成格
	@UpdateTimestamp  //自动生成
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;//到期时间     （因为需要外部录入数据，不能自动生成日期）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}