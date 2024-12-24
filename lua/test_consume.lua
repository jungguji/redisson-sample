-- 요청 수 제한
local max_requests = 10 -- 요청 수 제한
local request_count = 0 -- 현재 요청 수

request = function()
    if request_count >= max_requests then
        wrk.thread:stop() -- 요청 제한에 도달하면 쓰레드 중지
    end

    request_count = request_count + 1
--     local user_id = math.random(1, 5) -- 1~5 중 랜덤 ID 선택
    local user_id = 1
    local path = "/resources/" .. user_id .. "/consume"
    return wrk.format("POST", path)
end

-- wrk -t50 -c50 -d1s -s test_consume.lua http://125.128.148.5:8080
-- wrk -t10 -c200 -d1s -s test_consume.lua http://125.128.148.5:8080
