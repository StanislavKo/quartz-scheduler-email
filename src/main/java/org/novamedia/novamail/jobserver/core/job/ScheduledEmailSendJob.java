/**
 * ************************************************************
 * Copyright (c) 2015, Novamedia Oü.
 * All rights reserved.
 * <p/>
 * No Part of this file may be reproduced, stored in a retrieval
 * system, or transmitted, in any form, or by any means,
 * electronic, mechanical, photocopying, recording,
 * or otherwise, without the prior consent of Novamedia Oü
 *
 * @Author Joona Savolainen <js@novamail.ee>
 * ************************************************************
 */
package org.novamedia.novamail.jobserver.core.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ScheduledEmailSendJob extends Job {

    @JsonProperty("id")
    public Integer jobId;

    @JsonProperty("account_id")
    public Integer accountId;

    @JsonProperty("list_id")
    public Integer listId;

    @JsonProperty("sub_list_ids")
    public List<Integer> subListIds = new LinkedList<>();

    @JsonProperty("campaign_id")
    public Integer campaignId;

    @JsonProperty("email_id")
    public Integer emailId;

    @JsonProperty("email_body_id")
    public Integer emailBodyId;

    @JsonProperty("email_subject_id")
    public Integer emailSubjectId;

    @JsonProperty("send_profile_id")
    public Integer sendProfileId;

    @JsonProperty("mta_id")
    public Integer mtaId;

    @JsonProperty("send_at")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.YYYY")
    public Date sendAt;

    public ScheduledEmailSendJob() {
        super();
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getEmailBodyId() {
        return emailBodyId;
    }

    public void setEmailBodyId(Integer emailBodyId) {
        this.emailBodyId = emailBodyId;
    }

    public Integer getEmailSubjectId() {
        return emailSubjectId;
    }

    public void setEmailSubjectId(Integer emailSubjectId) {
        this.emailSubjectId = emailSubjectId;
    }

    public Integer getSendProfileId() {
        return sendProfileId;
    }

    public void setSendProfileId(Integer sendProfileId) {
        this.sendProfileId = sendProfileId;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    @Override
    @JsonIgnore 
    public Trigger getTrigger() {
        return TriggerBuilder.newTrigger().withIdentity(this.toString(), "scheduledEmailSendJob")
                .startAt(sendAt)
                .build();
    }
}
