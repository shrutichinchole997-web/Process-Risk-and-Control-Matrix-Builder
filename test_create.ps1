$token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc3ODI5OTUyMiwiZXhwIjoxNzc4Mzg1OTIyfQ.8OU6FA-dnzBKuLE72UNXoKpcftulR-R19qhImDW4kHw"
$headers = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $token"
}
$body = @{
    processName = "User Login Process"
    riskDescription = "Unauthorized access to user accounts"
    controlDescription = "Implement JWT and multi-factor authentication"
    priority = "HIGH"
    status = "COMPLETED"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/prc/create" -Method Post -Headers $headers -Body $body
