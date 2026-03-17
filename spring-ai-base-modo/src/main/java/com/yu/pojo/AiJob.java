package com.yu.pojo;

import com.yu.Enum.JobType;
import lombok.Data;

import java.util.Map;

@Data
public class AiJob {

    Map<String,String> keyInfos;

    JobType jobType;
}
