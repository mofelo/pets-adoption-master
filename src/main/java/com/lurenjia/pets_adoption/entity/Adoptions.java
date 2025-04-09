package com.lurenjia.pets_adoption.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lurenjia
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Adoptions implements Serializable {

    public Long getAdoId() {
        return adoId;
    }

    public void setAdoId(Long adoId) {
        this.adoId = adoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public LocalDate getAdoDate() {
        return adoDate;
    }

    public void setAdoDate(LocalDate adoDate) {
        this.adoDate = adoDate;
    }

    public Integer getAdoStatus() {
        return adoStatus;
    }

    public void setAdoStatus(Integer adoStatus) {
        this.adoStatus = adoStatus;
    }

    public String getAdoNote() {
        return adoNote;
    }

    public void setAdoNote(String adoNote) {
        this.adoNote = adoNote;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 领养记录id
     */
    @TableId(value = "ado_id", type = IdType.AUTO)
    private Long adoId;

    /**
     * 领养人id
     */
    private Long userId;

    /**
     * 宠物id
     */
    private Long petId;

    /**
     * 领养时间
     */
    private LocalDate adoDate;

    /**
     * 领养状态：0失败，1成功，2处理中
     */
    private Integer adoStatus;

    /**
     * 备注信息，领养情况
     */
    private String adoNote;

}
