$baseUrl = "http://localhost:8080/customers"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "          DBS-PROJ API TEST SUITE - ASSIGNMENT 10         " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

# 1. POST: Create a new Customer
Write-Host ">>> [1/3] POST /customers (Creating New Customer)..." -ForegroundColor Yellow
$postBody = @{
    firstName = "Ada"
    lastName  = "Lovelace"
    email     = "ada.lovelace@example.com"
    phone     = "+358401234567"
} | ConvertTo-Json

$createResponse = Invoke-RestMethod -Uri $baseUrl -Method Post -Body $postBody -ContentType "application/json"
$createResponse | ConvertTo-Json -Depth 3
Write-Host ""
Write-Host "----------------------------------------------------------" -ForegroundColor Gray

# Extract Created ID
$newId = $createResponse.id
if (-not $newId) { $newId = 1 }

# 2. GET: Retrieve Customers with Limit = 5
Write-Host ">>> [2/3] GET /customers?limit=5 (Fetching Limited Customers)..." -ForegroundColor Yellow
$getAllResponse = Invoke-RestMethod -Uri "${baseUrl}?limit=5" -Method Get
$getAllResponse | ConvertTo-Json -Depth 3
Write-Host ""
Write-Host "----------------------------------------------------------" -ForegroundColor Gray

# 3. GET: Retrieve Customer by ID
Write-Host ">>> [3/3] GET /customers/$newId (Fetching Customer by ID)..." -ForegroundColor Yellow
$getByIdResponse = Invoke-RestMethod -Uri "$baseUrl/$newId" -Method Get
$getByIdResponse | ConvertTo-Json -Depth 3
Write-Host ""

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "                   TEST SUITE COMPLETED                   " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
