var appUID;

document.getElementById('selectDevice').addEventListener("change", function () {
    getPacketData(appUID, true);
});
document.getElementById('selectLimit').addEventListener("change", function () {
    getPacketData(appUID, true);
});

function getPacketData(appID, extern) {
    appUID = appID;
    var limit = document.getElementById('selectLimit').value;
    console.log('limit is: ' + limit);
    var xhttp = new XMLHttpRequest();
    xhttp.timeout = 2000;
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var packetData = JSON.parse(this.responseText);
            if (packetData.hasOwnProperty('error')) {
                alert(packetData.error);
            } else {
                if (!extern) {
                    loadSelector(packetData);
                }
                var selectedDevice = document.getElementById('selectDevice').value;
                if (packetData.dataset.length > 0) {
                    loadPacketTable(packetData, getIndex(packetData, selectedDevice));
                    document.getElementById('packetCardGroup').style.display = "block";
                } else {
                    document.getElementById('packetCardGroup').style.display = "none";
                }

            }
        }
    };
    xhttp.ontimeout = function () {
        window.location.href = 'dashboard';
    }
    xhttp.open("POST", "getData.php", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("type=getNodeData&uid=" + appID + "&limit=" + limit);
}

function loadSelector(data) {
    var selector = document.getElementById('selectDevice');
    selector.innerHTML = "";
    for (i = 0; i < data.dataset.length; i++) {
        var option = document.createElement('option');
        option.innerHTML = data.dataset[i].name;
        selector.appendChild(option);
    }
}

function loadPacketTable(data, index) {
    var selectedDevice = document.getElementById('selectDevice').value;
    document.getElementById("packetDeviceName").innerHTML = selectedDevice;

    var table = document.getElementById("packetDeviceTable");
    var tableBody = document.getElementById("packetDeviceTableBody");
    tableBody.innerHTML = "";
    for (var i = 0; i < data.dataset[index].data.length; i++) {
        var row = tableBody.insertRow(-1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        cell1.innerHTML = formatTime(data.dataset[index].data[i].time);
        var viewBtn = document.createElement('button');
        viewBtn.className = 'btn btn-danger';
        viewBtn.type = 'button';
        viewBtn.innerHTML = 'View';
        viewBtn.dataset.obj = i
        viewBtn.dataset.device = index
        viewBtn.addEventListener('click', function () {
            var properties = data.dataset[this.dataset.device].data[this.dataset.obj];
            console.log(properties);
            document.getElementById('packetDeviceModalTitle').innerHTML = formatTime(properties.time);
            console.log(properties.rssi);
            document.getElementById('packet-RSSI').innerHTML = properties.rssi + ' dBm';
            document.getElementById('packet-Status').innerHTML = properties.stat;
            document.getElementById('packet-Size').innerHTML = properties.size + ' Bytes';
            document.getElementById('packet-Frequency').innerHTML = properties.freq + ' MHz';
            document.getElementById('packet-Data').value = atob(properties.data);
            $("#packetDeviceModal").modal('show');

        })
        cell2.appendChild(viewBtn);
    }
}

function getIndex(data, name) {
    for (i = 0; i < data.dataset.length; i++) {
        if (data.dataset[i].name == name) {
            console.log('found ' + name + ' on index: ' + i);
            return i;
        }
    }
    return -1;
}

function formatTime(time) {
    var formatted = time;
    var year = time.substring(0, 4);
    var month = time.replace(year, "")
    var month = month.substring(1, formatted.indexOf('-')).replace('-', "");
    var day = time.replace(year, '').replace(month, '')
    var day = day.replace('-', '').replace('-', '');
    var day = day.substring(0, day.indexOf('T'));
    var time = time.substring(time.indexOf("T") + 1).replace('Z', '');
    var time = time.substring(0, time.indexOf('.'));
    formatted = day + '-' + month + '-' + year + ' ' + time;
    return formatted;
}
