# High Efficiency Adaptive Data Streaming for IoT Service Framework

Internet of Things (IoT) nodes transfer continous stream of data to cloud-based servers. Due to network congestion, there are packet losses, resulting to incomplete or corrupt data transfers. To avoid loss of data packets, a caching mechanism is required between the IoT nodes and cloud-servers.

This is a class project that aims to implement an "intelligent" in-network caching node between IoT devices and cloud-based server. The caching node monitors the network load and data stream accordingly. When there is network congestion, caching node will store some data and will adjust network packet sizes accordingly.