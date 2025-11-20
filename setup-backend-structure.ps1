param(
    # Đường dẫn root project (chứa pom.xml)
    [string]$ProjectRoot = ".",
    # Base package của project
    [string]$BasePackage = "com.example.studentactivity"
)

# Convert package name -> folder path
$packagePath = $BasePackage -replace '\.', '\'

# Path thư mục service + impl
$serviceFolder = Join-Path $ProjectRoot "src\main\java\$packagePath\service"
$implFolder    = Join-Path $serviceFolder "impl"

# Tạo folder nếu chưa có
$folders = @($serviceFolder, $implFolder)

foreach ($f in $folders) {
    if (-not (Test-Path $f)) {
        New-Item -ItemType Directory -Path $f | Out-Null
        Write-Host "Created: $f"
    } else {
        Write-Host "Exists : $f"
    }
}

# Danh sách service mới
$services = @(
    "SinhVienService",
    "DiemDanhService",
    "DrlConfigService",
    "KhieuNaiService",
    "KhoaService",
    "LopService"
)

Write-Host "==> Creating service + impl classes..."

foreach ($svc in $services) {

    # File interface
    $interfacePath = Join-Path $serviceFolder "$svc.java"
    $interfaceContent = @"
package $BasePackage.service;

public interface $svc {

}
"@

    if (-not (Test-Path $interfacePath)) {
        $interfaceContent | Set-Content -Path $interfacePath -Encoding UTF8
        Write-Host "Created: $interfacePath"
    } else {
        Write-Host "Exists : $interfacePath"
    }

    # File implementation
    $implClass = "${svc}Impl"
    $implPath = Join-Path $implFolder "$implClass.java"
    $implContent = @"
package $BasePackage.service.impl;

import org.springframework.stereotype.Service;
import $BasePackage.service.$svc;

@Service
public class $implClass implements $svc {

}
"@

    if (-not (Test-Path $implPath)) {
        $implContent | Set-Content -Path $implPath -Encoding UTF8
        Write-Host "Created: $implPath"
    } else {
        Write-Host "Exists : $implPath"
    }
}

Write-Host "==> DONE. All new Service + Impl files created."
