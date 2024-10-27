package com.learnway.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.learnway.global.exceptions.S3Exception;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;
    
    // 업로드
    public String upload(MultipartFile image, String key) throws S3Exception {
        //입력 받은 파일이 비어있는지 검증
        if(image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
            throw new S3Exception();
        }
        return this.uploadImage(image, key);
    }

    private String uploadImage(MultipartFile image, String key) throws S3Exception {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image, key);
        } catch (IOException e) {
            throw new S3Exception("업로드 에러");
        }
    }

    //확장자 검증
    private void validateImageFileExtention(String filename) throws S3Exception {
        int lastDotIndex = filename.lastIndexOf(".");

        // .이 없는경우 exception
        if (lastDotIndex == -1) {
            throw new S3Exception("잘못된 확장자");
        }

        // 잘못된 확장자 exception
        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Exception("잘못된 확장자");
        }
    }

    // S3에 업로드
    private String uploadImageToS3(MultipartFile image, String key) throws IOException, S3Exception {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")+1); //확장자 명

        String s3FileName = key + UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        //image를 byte[]로 변환
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);

        //S3에 요청할 때 사용할 byteInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        //이미지 업로드
        try{
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest); // put image to S3
        }catch (Exception e){
            e.printStackTrace();
            throw new S3Exception("업로드 오류");
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return s3.getUrl(bucketName, s3FileName).toString();
    }

    //이미지 삭제
    public void deleteImageFromS3(String imageAddress) throws S3Exception {
        String key = getKeyFromImageAddress(imageAddress);
        try{
            s3.deleteObject(new DeleteObjectRequest(bucketName, key));
        }catch (Exception e){
            throw new S3Exception("이미지 삭제 오류");
        }
    }

    // 암호화된 이미지 경로 디코딩
    private String getKeyFromImageAddress(String imageAddress) throws S3Exception {
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new S3Exception("이미지 삭제 오류");
        }
    }
}