package com.gdh.shoppingmall.domain.product.registration

import com.gdh.shoppingmall.common.ParayoException
import com.gdh.shoppingmall.domain.product.ProductImage
import com.gdh.shoppingmall.domain.product.ProductImageRepository
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 이미지 저장 코드 로직
 * @Value -> application.yml 에 기입한 파일 업로드 디렉토리 설정을 읽어 애노테이션이 붙은 변수에 대입해주는 역할
 * MultipartFile -> 파일 업로드할 때 컨트롤러로 인입되는 업로드 파일 데이터 객체, 파일 정보와 저장할 때 사용되는 함수 준비
 * takeLastWhile() -> char -> boolean 타입의 함수를 인자로 받아, 함수의 반환값이 true가 될 때 까지 마지막 문자열을 반환
 *                    "." 을 기준으로 파일의 확장자를 구하는 용도로 사용
 * UUID.randomUUID() -> 파일명을 그대로 저장하면 서로 다른 사용자가 같은 이름을 가진 파일 업로드할 시 덮어쓸 위험이 존재하므로
 *                      랜덤 문자열을 파일명으로 사용해 저장
 * targetFile.parentFile.mkdirs() -> 파일이 저장될 디렉토리를 생성하는 함수
 * image.transferTo() -> MultipartFile 클래스에 선언된 함수, 업로드 파일을 파라미터로 지정된 파일 경로로 저장해주는 함수
 *                       /parayo/images/{년월일}/uuid.ext
 * Thumbnails.of() -> 썸네일 파일 저장
 */

@Service
class ProductImageService @Autowired constructor(
    private val productImageRepository: ProductImageRepository
) {
    @Value("\${parayo.file-upload-default-dir}")
    var uploadPath: String? = ""

    fun uploadImage(image: MultipartFile): ProductImageUploadResponse {
        val filePath = saveImageFile(image)
        val productImage = saveImageData(filePath)

        return productImage.id?.let {
            ProductImageUploadResponse(it, filePath)
        } ?: throw ParayoException("이미지 저장 실패. 다시 시도해주세요")
    }

    private fun saveImageFile(image: MultipartFile): String {
        val extension = image.originalFilename
            ?.takeLastWhile { it != '.' }
            ?: throw ParayoException("다른 이미지로 다시 시도해주세요.")

        val uuid = UUID.randomUUID().toString()
        val date = SimpleDateFormat("yyyyMMdd").format(Date())

        val filePath = "/images/$date/$uuid.$extension"
        val targetFile = File("$uploadPath/$filePath")
        val thumbnail = targetFile.absolutePath
            .replace(uuid, "$uuid-thumb")
            .let(::File)

        targetFile.parentFile.mkdirs()
        image.transferTo(targetFile)

        Thumbnails.of(targetFile)
            .crop(Positions.CENTER)
            .size(300, 300)
            .outputFormat("jpg")
            .outputQuality(0.8f)
            .toFile(thumbnail)

        return filePath
    }

    private fun saveImageData(filePath: String): ProductImage {
        val productImage = ProductImage(filePath)
        return productImageRepository.save(productImage)
    }
}