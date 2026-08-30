param(
  [string]$root,
  [string]$out
)
$sb = New-Object System.Text.StringBuilder
$files = Get-ChildItem -Path $root -Recurse -Filter *.java | Sort-Object FullName
foreach ($f in $files) {
  $lines = [System.IO.File]::ReadAllLines($f.FullName)
  [void]$sb.AppendLine("==== $($f.FullName) ====")
  $inBlock = $false
  for ($i = 0; $i -lt $lines.Count; $i++) {
    $line = $lines[$i]
    $ln = [string]($i + 1)
    $pos = 0
    if ($inBlock) {
      $endIdx = $line.IndexOf('*/')
      if ($endIdx -ge 0) {
        $t = $line.Substring(0, $endIdx).Trim()
        if ($t) { [void]$sb.AppendLine("${ln}: $t") }
        $inBlock = $false
        $pos = $endIdx + 2
      } else {
        $t = $line.Trim()
        if ($t) { [void]$sb.AppendLine("${ln}: $t") }
        continue
      }
    }
    while ($pos -lt $line.Length) {
      $bc = $line.IndexOf('/*', $pos)
      $hc = $line.IndexOf('//', $pos)
      if ($hc -ge 0 -and ($bc -lt 0 -or $bc -gt $hc)) {
        $t = $line.Substring($hc + 2).Trim()
        if ($t) { [void]$sb.AppendLine("${ln}: $t") }
        break
      }
      if ($bc -ge 0) {
        $endIdx = $line.IndexOf('*/', $bc + 2)
        if ($endIdx -ge 0) {
          $t = $line.Substring($bc + 2, $endIdx - $bc - 2).Trim()
          if ($t -and $t -ne '*') { [void]$sb.AppendLine("${ln}: $t") }
          $pos = $endIdx + 2
        } else {
          $t = $line.Substring($bc + 2).Trim()
          if ($t -and $t -ne '*') { [void]$sb.AppendLine("${ln}: $t") }
          $inBlock = $true
          break
        }
      } else {
        break
      }
    }
  }
}
[System.IO.File]::WriteAllText($out, $sb.ToString())
$count = ($sb.ToString() -split "`n").Count
Write-Host "wrote $out ($count lines)"