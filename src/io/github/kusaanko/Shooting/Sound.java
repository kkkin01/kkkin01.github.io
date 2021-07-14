package io.github.kusaanko.Shooting;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    public Clip createClip(File path) {
        //�w�肳�ꂽURL�̃I�[�f�B�I���̓X�g���[�����擾
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){

            //�t�@�C���̌`���擾
            AudioFormat af = ais.getFormat();

            //�P��̃I�[�f�B�I�`�����܂ގw�肵����񂩂�f�[�^���C���̏��I�u�W�F�N�g���\�z
            DataLine.Info dataLine = new DataLine.Info(Clip.class,af);

            //�w�肳�ꂽ Line.Info �I�u�W�F�N�g�̋L�q�Ɉ�v���郉�C�����擾
            Clip c = (Clip)AudioSystem.getLine(dataLine);

            //�Đ���������
            c.open(ais);

            return c;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }
}