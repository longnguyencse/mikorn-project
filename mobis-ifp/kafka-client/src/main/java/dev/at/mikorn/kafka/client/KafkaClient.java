package dev.at.mikorn.kafka.client;



import dev.at.mikorn.kafka.client.constants.IKafkaConstants;
import dev.at.mikorn.kafka.client.consumer.ConsumerCreator;
import dev.at.mikorn.kafka.client.producer.ProducerCreator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.ExecutionException;

public class KafkaClient {
	public static void main(String[] args) {
		runProducer("kalog");
//		runConsumer();
	}

	static void runConsumer() {
		Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

		int noMessageToFetch = 0;

		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);
			if (consumerRecords.count() == 0) {
				noMessageToFetch++;
				if (noMessageToFetch > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
					break;
				else
					continue;
			}

			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			});
			consumer.commitAsync();
		}
		consumer.close();
	}

	public static void runProducer(String result) {
		Producer<Long, String> producer = ProducerCreator.createProducer();
		final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>
				(IKafkaConstants.TOPIC_NAME, result);
		try {
			RecordMetadata metadata = producer.send(record).get();
			System.out.println("Record sent with key: *** " + " to partition " + metadata.partition()
					+ " with offset " + metadata.offset());

		}catch (InterruptedException e ) {
			System.out.println("Error in sending record");
			System.out.println(e);
		} catch (ExecutionException e) {
			System.out.println("Error in sending record");
			System.out.println(e);
		}

		/*for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {
			final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
					"This is record " + index + result);
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
						+ " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
		}*/
	}
}
