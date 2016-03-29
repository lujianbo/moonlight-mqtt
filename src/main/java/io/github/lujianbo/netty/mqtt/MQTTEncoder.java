package io.github.lujianbo.netty.mqtt;

import io.github.lujianbo.mqtt.domain.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.UnsupportedEncodingException;

public class MQTTEncoder extends MessageToByteEncoder<MQTTMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MQTTMessage msg, ByteBuf out) throws Exception {
        if (msg instanceof ConnectMessage) {
            ByteBuf variableHeader=ctx.alloc().buffer(10);
            ByteBuf payLoad=ctx.alloc().buffer();
            try {
                ConnectMessage message = (ConnectMessage) msg;
                //Protocol Name
                variableHeader.writeBytes(encodeString(message.getProtocolName()));
                //Protocol Level
                variableHeader.writeByte(message.getProtocolLevel());

                //flags
                byte connectionFlags = 0;
                if (message.isCleanSession()) {connectionFlags |= 0x02;}
                if (message.isWillFlag()) {connectionFlags |= 0x04;}
                connectionFlags |= ((message.getWillQos() & 0x03) << 3);
                if (message.isWillRetain()) { connectionFlags |= 0x020;}
                if (message.isPasswordFlag()) {connectionFlags |= 0x040;}
                if (message.isUserFlag()) {connectionFlags |= 0x080;}
                variableHeader.writeByte(connectionFlags);

                //keepAlive
                variableHeader.writeShort(message.getKeepAlive());

                //payLoad
                if (message.getClientId()!=null){
                    payLoad.readBytes(encodeString(message.getClientId()));
                    if (message.isWillFlag()) {
                        payLoad.writeBytes(encodeString(message.getWillTopic()));
                        payLoad.writeBytes(encodeFixedLengthContent(message.getWillMessage()));
                    }
                    if (message.isUserFlag() && message.getUserName() != null) {
                        payLoad.writeBytes(encodeString(message.getUserName()));
                        if (message.isPasswordFlag() && message.getPassword() != null) {
                            payLoad.writeBytes(encodeFixedLengthContent(message.getPassword()));
                        }
                    }
                }
                //写入数据
                out.writeByte(MQTTMessage.CONNECT << 4);
                //写入长度
                out.writeBytes(encodeRemainingLength(variableHeader.readableBytes()+payLoad.readableBytes()));
                //可变头部
                out.writeBytes(variableHeader);
                //payload
                out.writeBytes(payLoad);
            }finally {
                variableHeader.release();
                payLoad.release();
            }
            return;
        }
        if (msg instanceof ConnackMessage) {
            ConnackMessage message=(ConnackMessage)msg;

            out.writeByte(MQTTMessage.CONNACK << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeByte(message.isSessionPresentFlag() ? 0x01 : 0x00);
            out.writeByte(message.getReturnCode());
            return;
        }
        if (msg instanceof PublishMessage) {
            PublishMessage message=(PublishMessage)msg;
            ByteBuf variableHeader=ctx.alloc().buffer(10);
            //flags
            byte flags = 0;
            if (message.isDupFlag()) {flags |= 0x08;}
            if (message.isRetainFlag()) {flags |= 0x01;}
            flags |= ((message.getQosLevel() & 0x03) << 1);

            //variableHeader
            variableHeader.writeBytes(encodeString(message.getTopicName()));//topic
            variableHeader.writeShort(message.getPacketIdentifier());//packet id

            //write
            out.writeByte(MQTTMessage.PUBLISH<< 4 | flags);
            out.writeBytes(encodeRemainingLength(variableHeader.readableBytes()+message.getPayload().length));
            out.writeBytes(variableHeader);
            out.writeBytes(message.getPayload());

            //release
            variableHeader.release();
            return;
        }
        if (msg instanceof PubackMessage) {
            PubackMessage message=(PubackMessage)msg;
            out.writeByte(MQTTMessage.PUBACK<< 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
        }
        if (msg instanceof PubrecMessage) {
            PubrecMessage message=(PubrecMessage)msg;
            out.writeByte(MQTTMessage.PUBREC<< 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }
        if (msg instanceof PubrelMessage) {
            PubrelMessage message=(PubrelMessage)msg;
            out.writeByte(MQTTMessage.PUBREL<< 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }

        if (msg instanceof PubcompMessage) {
            PubcompMessage message=(PubcompMessage)msg;
            out.writeByte(MQTTMessage.PUBCOMP<< 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }

        if (msg instanceof SubscribeMessage) {
            SubscribeMessage message=(SubscribeMessage)msg;

            ByteBuf payload=ctx.alloc().buffer();
            for (SubscribeMessage.TopicFilterQoSPair pair :message.getPairs()){
                payload.writeBytes(encodeString(pair.getTopicName()));
                payload.writeByte(pair.getQos());
            }
            out.writeByte(MQTTMessage.SUBSCRIBE << 4);
            out.writeBytes(encodeRemainingLength(2+payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }
        if (msg instanceof SubackMessage) {
            SubackMessage message=(SubackMessage)msg;

            ByteBuf payload=ctx.alloc().buffer();
            for (byte b:message.getReturnCodes()){
                payload.writeByte(b);
            }
            out.writeByte(MQTTMessage.SUBACK << 4);
            out.writeBytes(encodeRemainingLength(2+payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }
        if (msg instanceof UnsubscribeMessage) {
            UnsubscribeMessage message=(UnsubscribeMessage)msg;

            ByteBuf payload=ctx.alloc().buffer();
            for (String topicName:message.getTopicNames()){
                payload.writeBytes(encodeString(topicName));
            }
            out.writeByte(MQTTMessage.UNSUBSCRIBE << 4);
            out.writeBytes(encodeRemainingLength(2+payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }

        if (msg instanceof UnsubackMessage) {
            UnsubackMessage message = (UnsubackMessage) msg;
            out.writeByte(MQTTMessage.UNSUBACK << 4)
                    .writeBytes(encodeRemainingLength(2))
                    .writeShort(message.getPacketIdentifier());
            return;
        }
        if (msg instanceof PingreqMessage) {
            out.writeByte(MQTTMessage.PINGREQ << 4).writeByte(0);
            return;
        }
        if (msg instanceof PingrespMessage) {
            out.writeByte(MQTTMessage.PINGRESP << 4).writeByte(0);
            return;
        }
        if (msg instanceof DisconnectMessage) {
            out.writeByte(MQTTMessage.DISCONNECT << 4).writeByte(0);
            ctx.close();
            return;
        }
        /**
         * ERROR
         * */

    }

    private ByteBuf encodeString(String str) {
        byte[] raw;
        try {
            raw = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        return encodeFixedLengthContent(raw);
    }

    private  ByteBuf encodeFixedLengthContent(byte[] content) {
        ByteBuf out = Unpooled.buffer(2);
        out.writeShort(content.length);
        out.writeBytes(content);
        return out;
    }

    private ByteBuf encodeRemainingLength(int value) {
        ByteBuf encoded = Unpooled.buffer(4);
        byte digit;
        do {
            digit = (byte) (value % 128);
            value = value / 128;
            // if there are more digits to encode, set the top bit of this digit
            if (value > 0) {
                digit = (byte) (digit | 0x80);
            }
            encoded.writeByte(digit);
        } while (value > 0);
        return encoded;
    }

}
